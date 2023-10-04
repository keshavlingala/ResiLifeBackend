package codes.keshav.home.management.auth

import codes.keshav.home.management.utils.JwtTokenUtil
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthenticationFilter(
	private val jwtTokenUtil: JwtTokenUtil,
	private val authenticationManager: AuthenticationManager
) : OncePerRequestFilter() {
	override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
		val auth = request.getHeader("Authorization")
		if (auth != null && auth.startsWith("Bearer ")) {
			val authToken = auth.substring(7)
			val username = jwtTokenUtil.getEmail(authToken)
			if (username != null && jwtTokenUtil.isTokenValid(authToken)) {
				val authentication = UsernamePasswordAuthenticationToken(username, null, emptyList())
				authenticationManager.authenticate(authentication)
			}
		}

		filterChain.doFilter(request, response)
	}
}
