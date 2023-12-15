package codes.keshav.home.management.controller

import codes.keshav.home.management.dto.Bearer
import codes.keshav.home.management.dto.response.SplitWiseExpenseResponse
import codes.keshav.home.management.dto.response.UserDataResponse
import codes.keshav.home.management.retrofit.CanvasApi
import codes.keshav.home.management.retrofit.Postgrest
import codes.keshav.home.management.service.AuthService
import codes.keshav.home.management.service.SplitWiseService
import codes.keshav.home.management.utils.validatedExecute
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("user")
class UserController(
	private val postgRest: Postgrest,
	val authService: AuthService,
	val splitWiseService: SplitWiseService,
	val canvasApi: CanvasApi
) {

	@GetMapping("me")
	fun getMe(): UserDataResponse {
		return authService.getCurrentUser().toResponse()
	}

	@GetMapping("expenses")
	fun getExpenses(): SplitWiseExpenseResponse {
		return splitWiseService.getExpenses()
	}

	@GetMapping("assignments")
	fun getAssignments(): List<Any> {
		val user = authService.getCurrentUser()
		val token = user.meta?.canvasApiKey ?: throw Exception("Canvas API key not found")
		val courseList = canvasApi.getCourses(Bearer(token)).validatedExecute()
		val contextCodes = courseList.associate {
			"context_codes[]" to "course_${it.id}"
		}
		return canvasApi.getCalendarEvents(
			Bearer(token), mapOf(
				"start_date" to LocalDate.now().minusDays(7).toString(),
				"end_date" to LocalDate.now().plusDays(60).toString(),
				"type" to "assignment"
			) + contextCodes
		).validatedExecute()
	}

}
