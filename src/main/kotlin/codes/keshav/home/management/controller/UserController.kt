package codes.keshav.home.management.controller

import codes.keshav.home.management.dto.response.UserDataResponse
import codes.keshav.home.management.retrofit.Postgrest
import codes.keshav.home.management.service.AuthService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("user")
class UserController(
	private val postgRest: Postgrest,
	val authService: AuthService
) {

	@GetMapping("me")
	fun getMe(): UserDataResponse {
		return authService.getCurrentUser().toResponse()
	}
}