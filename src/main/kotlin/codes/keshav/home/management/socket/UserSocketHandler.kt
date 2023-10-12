package codes.keshav.home.management.socket

import codes.keshav.home.management.dto.EqualParam
import codes.keshav.home.management.dto.SocketMessageType
import codes.keshav.home.management.dto.UserData
import codes.keshav.home.management.dto.response.UserDataResponse
import codes.keshav.home.management.retrofit.Postgrest
import codes.keshav.home.management.utils.Utils.getObject
import codes.keshav.home.management.utils.toJsonString
import codes.keshav.home.management.utils.validatedExecute
import org.springframework.stereotype.Service
import org.springframework.web.socket.TextMessage
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.handler.TextWebSocketHandler

@Service
class UserSocketHandler(
	val postgrest: Postgrest
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
				sendToUser("update", res.toResponse())
			}
		}
	}

	private fun sendToUser(type: String, user: UserDataResponse) {
		val message = SocketMessageType(type, user).toJsonString()
		try {
			sessionMap[user.email]?.sendMessage(TextMessage(message))
		} catch (e: Exception) {
			println("Error sending message to user: ${user.email}")
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

	fun sendToUser(userId: String, message: String) {
		sessionMap[userId]?.sendMessage(TextMessage(message))
	}

	fun updateUserData(user: UserData) {
		val message = user.toResponse().toJsonString()
		try {
			sessionMap[user.email]?.sendMessage(TextMessage(message))
		} catch (e: Exception) {
			println("Error sending message to user: ${user.email}")
		}
	}
}