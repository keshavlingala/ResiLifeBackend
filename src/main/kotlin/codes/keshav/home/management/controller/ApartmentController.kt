package codes.keshav.home.management.controller

import codes.keshav.home.management.dto.request.Apartment
import codes.keshav.home.management.dto.request.ApartmentRequest
import codes.keshav.home.management.service.ApartmentService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("apt")
class ApartmentController(
	val apartmentService: ApartmentService
) {
	@PostMapping("create")
	fun createApt(@RequestBody request: ApartmentRequest): Apartment {
		return apartmentService.createApt(request)
	}
}