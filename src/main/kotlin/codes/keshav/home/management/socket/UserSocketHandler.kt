package codes.keshav.home.management.socket

import codes.keshav.home.management.dto.EqualParam
import codes.keshav.home.management.dto.SocketMessageType
import codes.keshav.home.management.dto.UserData
import codes.keshav.home.management.dto.response.UserDataResponse
import codes.keshav.home.management.retrofit.Postgrest
import codes.keshav.home.management.service.AuthService
import codes.keshav.home.management.utils.Utils.getObject
import codes.keshav.home.management.utils.toJsonString
import codes.keshav.home.management.utils.validatedExecute
import org.springframework.stereotype.Service
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

@Service
class UserSocketHandler(
	val postgrest: Postgrest,
	val authService: AuthService
) : TextWebSocketHandler() {
	override fun handleTextMessage(session: WebSocketSession, message: TextMessage) {
		val msg = message.payload.getObject<SocketMessageType<UserDataResponse>>()
		println("Received message: ${message.payload}")
		if (msg == null) {
			println("Invalid message received")
			return
		}
		when (msg.type) {
			"update" -> {
				val res = postgrest.updateUser(EqualParam(msg.data.email), msg.data).validatedExecute().firstOrNull()
					?: throw Exception("User update failed")
				updateUserData("update", res.toResponse())
			}

			"get-expenses" -> {
				println("Get expenses message received")
				if (msg.data.apartmentId == null) {
					println("Apartment id is null")
					return
				}
				if (msg.data.meta?.splitwiseApiKey == null) {
					println("Splitwise api key is null")
					return
				}

			}

			"keys-update" -> {
				println("Keys update message received")
				val res = postgrest.updateUser(EqualParam(msg.data.email), msg.data).validatedExecute().firstOrNull()
					?: throw Exception("User update failed")
				updateUserData("update", res.toResponse())
			}

			else -> {
				println("Invalid User Socket Message")
				updateUserData("invalid", null)
			}
		}
	}


	private val sessionMap = mutableMapOf<String, WebSocketSession>()

	override fun afterConnectionEstablished(session: WebSocketSession) {
		println("Session attributes: ${session.attributes}")
		val user = session.attributes["user"] as UserData
		sessionMap[user.email] = session
		println("Connection established")
		session.sendMessage(TextMessage(user.toResponse().toJsonString()))
	}

	fun updateUserData(type: String, user: UserDataResponse?) {
		val message = SocketMessageType(type, user).toJsonString()
		try {
			sessionMap[user?.email]?.sendMessage(TextMessage(message))
		} catch (e: Exception) {
			println("Error sending message to user: ${user?.email}")
		}
	}
}