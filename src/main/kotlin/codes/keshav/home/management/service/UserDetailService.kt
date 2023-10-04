package codes.keshav.home.management.service

import codes.keshav.home.management.dto.EqualParam
import codes.keshav.home.management.dto.UserSecurity
import codes.keshav.home.management.retrofit.Postgrest
import codes.keshav.home.management.utils.validatedExecute
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailsService(
	val postgrest: Postgrest
) : UserDetailsService {
	override fun loadUserByUsername(username: String): UserDetails {
		val user = postgrest.getUserByName(EqualParam(username)).validatedExecute().firstOrNull()
			?: throw Exception("User not found!")
		return UserSecurity(
			user.id,
			user.email,
			user.password
		)
	}
}