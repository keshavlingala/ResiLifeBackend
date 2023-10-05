package codes.keshav.home.management.controller

import codes.keshav.home.management.dto.JwtResponse
import codes.keshav.home.management.dto.LoginDto
import codes.keshav.home.management.dto.UserData
import codes.keshav.home.management.service.AuthService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("auth")
class AuthController(
	val authService: AuthService
) {
	@PostMapping("token")
	fun generateToken(
		@RequestBody login: LoginDto
	): JwtResponse {
		return authService.verifyUserByLogin(login)
	}

	@PostMapping("signup")
	fun signUp(
		@RequestBody userData: UserData
	): JwtResponse {
		return authService.signUp(userData)
	}
}
