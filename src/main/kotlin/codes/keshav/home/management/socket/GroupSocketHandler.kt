package codes.keshav.home.management.socket

import codes.keshav.home.management.dto.EqualParam
import codes.keshav.home.management.dto.SocketMessageType
import codes.keshav.home.management.dto.UserData
import codes.keshav.home.management.dto.request.Apartment
import codes.keshav.home.management.dto.request.MemberDetails
import codes.keshav.home.management.retrofit.Postgrest
import codes.keshav.home.management.utils.Utils.getObject
import codes.keshav.home.management.utils.toJsonString
import codes.keshav.home.management.utils.validatedExecute
import org.springframework.stereotype.Service
import org.springframework.web.socket.CloseStatus
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler
import java.util.*

@Service
class GroupSocketHandler(
	val postgrest: Postgrest
) : TextWebSocketHandler() {
	override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
		val msg = message.payload.getObject<SocketMessageType<Apartment>>()
		if (msg == null) {
			println("Invalid message received")
			return
		}
		when (msg.type) {
			"update" -> {
				println("Update message received")
				val res = postgrest.updateApt(EqualParam(msg.data.apartmentId.toString()), msg.data)
					.validatedExecute().firstOrNull()
					?: throw Exception("Apartment update failed")
				updateApartment("update", res)
			}

			"get-members" -> {
				println("Get members message received")
				val res = postgrest.getApt(EqualParam(msg.data.apartmentId.toString()))
					.validatedExecute().firstOrNull()
					?: throw Exception("Apartment update failed")
				res.payload?.memberDetails = res.members?.map { email ->
					val user = postgrest.getUser(EqualParam(email))
						.validatedExecute().firstOrNull()
						?: throw Exception("User not found")
					MemberDetails(
						email = user.email,
						name = user.firstName + " " + user.lastName,
						picture = user.meta?.picture ?: ""
					)
				}
				val updateDB = postgrest.updateApt(EqualParam(msg.data.apartmentId.toString()), res)
					.validatedExecute().firstOrNull()
					?: throw Exception("Apartment update failed")
				updateApartment("update", updateDB)
			}
		}

	}

	private val sessionMap = mutableMapOf<UUID, MutableSet<WebSocketSession>>()

	override fun afterConnectionEstablished(session: WebSocketSession) {
		println("Session attributes: ${session.attributes}")
		val user = session.attributes["user"] as UserData
		user.apartmentId?.let {
			sessionMap[it] = sessionMap[it] ?: mutableSetOf()
			sessionMap[it]?.add(session)
			println("Connection established")
		}
		val apartment = postgrest.getApt(EqualParam(user.apartmentId.toString()))
			.validatedExecute().firstOrNull()
			?: throw Exception("Apartment not found")
		session.sendMessage(TextMessage(apartment.toJsonString()))
	}

	override fun afterConnectionClosed(session: WebSocketSession, status: CloseStatus) {
		println("Session attributes: ${session.attributes}")
		val user = session.attributes["user"] as UserData
		user.apartmentId?.let {
			sessionMap[it]?.remove(session)
			println("Connection closed")
		}
	}

	fun updateApartment(type: String, apartment: Apartment) {
		val message = SocketMessageType(
			type,
			apartment
		).toJsonString()
		try {
			sessionMap[apartment.apartmentId]?.forEach {
				it.sendMessage(TextMessage(message))
			}
		} catch (e: Exception) {
			println("Error sending message to apartment: ${apartment.apartmentId}")
		}
	}


}
