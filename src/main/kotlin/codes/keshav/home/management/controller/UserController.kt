package codes.keshav.home.management.controller

import codes.keshav.home.management.dto.response.SplitWiseExpenseResponse
import codes.keshav.home.management.dto.response.UserDataResponse
import codes.keshav.home.management.retrofit.Postgrest
import codes.keshav.home.management.service.ApartmentService
import codes.keshav.home.management.service.AuthService
import codes.keshav.home.management.service.SplitWiseService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("user")
class UserController(
	private val postgRest: Postgrest,
	val authService: AuthService,
	val apartmentService: ApartmentService,
	val splitWiseService: SplitWiseService
) {

	@GetMapping("me")
	fun getMe(): UserDataResponse {
		return authService.getCurrentUser().toResponse()
	}

	@GetMapping("expenses")
	fun getExpenses(): SplitWiseExpenseResponse {
		return splitWiseService.getExpenses()
	}
}
