package codes.keshav.home.management.auth

import codes.keshav.home.management.dto.EqualParam
import codes.keshav.home.management.retrofit.Postgrest
import codes.keshav.home.management.utils.validatedExecute
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class AuthUserDetailsService(private val postgrest: Postgrest) : UserDetailsService {
	override fun loadUserByUsername(username: String): UserDetails {
		val user = postgrest.getUserByName(EqualParam(username)).validatedExecute().firstOrNull()
			?: throw Exception("User not found!")
		return User
			.withUsername(user.username)
			.password(user.password)
			.authorities(emptyList())
			.build()
	}
}
