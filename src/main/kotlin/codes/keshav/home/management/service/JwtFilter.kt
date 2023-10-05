package codes.keshav.home.management.service

import codes.keshav.home.management.dto.EqualParam
import codes.keshav.home.management.retrofit.Postgrest
import codes.keshav.home.management.utils.JwtTokenUtil
import codes.keshav.home.management.utils.validatedExecute
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JwtFilter(
	private val postgrest: Postgrest,
	private val jwtTokenUtil: JwtTokenUtil
) : OncePerRequestFilter() {
	override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
		val header = request.getHeader("Authorization")
		if (header == null || !header.startsWith("Bearer ")) {
			filterChain.doFilter(request, response)
			return
		}
		val token = header.substring(7)
		val email = jwtTokenUtil.getUserNameFromToken(token)
		val user = postgrest.getUser(EqualParam(email)).validatedExecute().firstOrNull()
		if (user == null || !jwtTokenUtil.verifyJWT(token)) {
			filterChain.doFilter(request, response)
			return
		}
		val authentication = UsernamePasswordAuthenticationToken(user, null, null)
		SecurityContextHolder.getContext().authentication = authentication
		filterChain.doFilter(request, response)
	}
}