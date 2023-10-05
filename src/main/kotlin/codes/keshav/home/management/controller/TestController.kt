package codes.keshav.home.management.controller

import codes.keshav.home.management.service.AuthService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class TestController(
	val authService: AuthService
) {
	@GetMapping("/test")
	fun test(): String {
		val user = authService.getCurrentUser()
		println(user)
		return "Test Controller Reached!"
	}
}