package codes.keshav.home.management.config

import codes.keshav.home.management.socket.GroupSocketHandler
import codes.keshav.home.management.socket.HandShakeAuth
import codes.keshav.home.management.socket.UserSocketHandler
import org.springframework.context.annotation.Configuration
import org.springframework.web.socket.config.annotation.EnableWebSocket
import org.springframework.web.socket.config.annotation.WebSocketConfigurer
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry

@Configuration
@EnableWebSocket
class WebSocketConfig(
	val userSocketHandler: UserSocketHandler,
	val handShakeAuth: HandShakeAuth,
	val groupSocketHandler: GroupSocketHandler
) : WebSocketConfigurer {
	override fun registerWebSocketHandlers(registry: WebSocketHandlerRegistry) {
		registry.addHandler(userSocketHandler, "/ws/user")
			.setHandshakeHandler(handShakeAuth)
			.setAllowedOriginPatterns("*")

		registry.addHandler(groupSocketHandler, "/ws/group")
			.setHandshakeHandler(handShakeAuth)
			.setAllowedOriginPatterns("*")
	}
}