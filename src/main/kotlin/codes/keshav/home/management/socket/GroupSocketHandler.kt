package codes.keshav.home.management.socket

import codes.keshav.home.management.dto.EqualParam
import codes.keshav.home.management.dto.UserData
import codes.keshav.home.management.dto.request.Apartment
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
		println("Received message: ${message.payload}")
		val groupData = message.payload.getObject<Apartment>()
		if (groupData == null) {
			println("Invalid message received")
			return
		}
		val res = postgrest.updateApt(EqualParam(groupData.apartmentId.toString()), groupData)
			.validatedExecute().firstOrNull()
			?: throw Exception("Apartment update failed")
		updateApartment(res)
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

	fun updateApartment(apartment: Apartment) {
		val message = apartment.toJsonString()
		try {
			sessionMap[apartment.apartmentId]?.forEach {
				it.sendMessage(TextMessage(message))
			}
		} catch (e: Exception) {
			println("Error sending message to apartment: ${apartment.apartmentId}")
		}
	}


}
