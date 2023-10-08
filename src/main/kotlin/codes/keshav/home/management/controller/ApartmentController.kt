package codes.keshav.home.management.controller

import codes.keshav.home.management.dto.request.Apartment
import codes.keshav.home.management.dto.request.ApartmentRequest
import codes.keshav.home.management.service.ApartmentService
import org.springframework.web.bind.annotation.*
import java.util.*

@RestController
@RequestMapping("apt")
class ApartmentController(
	val apartmentService: ApartmentService
) {
	@PostMapping("create")
	fun createApt(@RequestBody request: ApartmentRequest): Apartment {
		return apartmentService.createApt(request)
	}

	@GetMapping
	fun getApt(): Apartment {
		return apartmentService.getApt()
	}

	@GetMapping("join/{aptId}")
	fun joinApt(
		@PathVariable("aptId") aptId: String
	): Apartment {
		println("Joining apartment	$aptId")
		return apartmentService.joinApt(UUID.fromString(aptId))
	}
}