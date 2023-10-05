package codes.keshav.home.management.service

import codes.keshav.home.management.dto.EqualParam
import codes.keshav.home.management.retrofit.Postgrest
import codes.keshav.home.management.utils.validatedExecute
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class SecurityUser(
	val postgrest: Postgrest
) : UserDetailsService {
	override fun loadUserByUsername(email: String): UserDetails {
		val user =
			postgrest.getUser(EqualParam(email)).validatedExecute().firstOrNull() ?: throw Exception("User not found")
		return User.withUsername(user.email)
			.password(user.password)
			.roles("USER")
			.build()
	}
}