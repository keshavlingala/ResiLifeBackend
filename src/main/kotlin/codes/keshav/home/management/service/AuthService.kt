package codes.keshav.home.management.service

import codes.keshav.home.management.dto.EqualParam
import codes.keshav.home.management.dto.JwtResponse
import codes.keshav.home.management.dto.LoginDto
import codes.keshav.home.management.dto.UserData
import codes.keshav.home.management.exceptions.AuthException
import codes.keshav.home.management.exceptions.UserNotFoundException
import codes.keshav.home.management.retrofit.Postgrest
import codes.keshav.home.management.utils.JwtTokenUtil
import codes.keshav.home.management.utils.validatedExecute
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
	val postgrest: Postgrest,
	val passwordEncoder: PasswordEncoder,
	val jwtTokenUtil: JwtTokenUtil,
) {
	fun getCurrentUser(): UserData {
		return SecurityContextHolder.getContext().authentication.principal as UserData
	}

	fun verifyUserByLogin(login: LoginDto): JwtResponse {
		val user = postgrest.getUser(EqualParam(login.email)).validatedExecute().firstOrNull()
			?: throw UserNotFoundException("User not found")
		if (passwordEncoder.matches(login.password, user.password)) {
			return JwtResponse(jwtTokenUtil.generateToken(user.email))
		} else {
			throw AuthException("Invalid password")
		}
	}

	fun signUp(userData: UserData): JwtResponse {
		userData.password = passwordEncoder.encode(userData.password)
		val user = postgrest.getUser(EqualParam(userData.email)).validatedExecute().firstOrNull()
		if (user != null) {
			throw AuthException("User already exists")
		} else {
			postgrest.addUser(userData).validatedExecute()
			return JwtResponse(jwtTokenUtil.generateToken(userData.email))
		}
	}

	fun updateUser(user: UserData) {
		postgrest.updateUser(EqualParam(user.email), user.toResponse()).validatedExecute()
	}
}