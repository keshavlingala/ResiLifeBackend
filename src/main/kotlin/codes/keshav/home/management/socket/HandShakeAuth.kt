package codes.keshav.home.management.socket

import codes.keshav.home.management.dto.EqualParam
import codes.keshav.home.management.exceptions.WebSocketAuthenticationException
import codes.keshav.home.management.retrofit.Postgrest
import codes.keshav.home.management.service.AuthService
import codes.keshav.home.management.utils.JwtTokenUtil
import codes.keshav.home.management.utils.validatedExecute
import org.springframework.http.server.ServerHttpRequest
import org.springframework.stereotype.Component
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.server.support.DefaultHandshakeHandler
import java.security.Principal

@Component
class HandShakeAuth(
	val authService: AuthService,
	val jwtTokenUtil: JwtTokenUtil,
	val postgrest: Postgrest
) : DefaultHandshakeHandler() {
	override fun determineUser(
		request: ServerHttpRequest,
		wsHandler: WebSocketHandler,
		attributes: MutableMap<String, Any>
	): Principal? {
//		Get token from query param token:
		val token = request.uri.query?.split("=")?.get(1)
		if (token == null) {
			println("Hand shake failed")
			throw WebSocketAuthenticationException("Invalid token")
		}
		val email = jwtTokenUtil.getUserNameFromToken(token)
		val user = postgrest.getUser(EqualParam(email)).validatedExecute().firstOrNull()
			?: throw WebSocketAuthenticationException("User not found")
		attributes["token"] = token
		attributes["user"] = user
		return super.determineUser(request, wsHandler, attributes)
	}
}