package codes.keshav.home.management.service

import codes.keshav.home.management.dto.request.Apartment
import codes.keshav.home.management.dto.request.ApartmentRequest
import codes.keshav.home.management.dto.request.CreateAptRequest
import codes.keshav.home.management.exceptions.ApartmentCreationException
import codes.keshav.home.management.retrofit.Postgrest
import codes.keshav.home.management.utils.validatedExecute
import org.springframework.stereotype.Service

@Service
class ApartmentService(
	val postgrest: Postgrest,
	val authService: AuthService
) {

	fun createApt(request: ApartmentRequest): Apartment {
		val user = authService.getCurrentUser()
		val apartment = postgrest.createApt(
			CreateAptRequest(
				name = request.name,
				members = listOf(user.email)
			)
		).validatedExecute().firstOrNull() ?: throw ApartmentCreationException("Apartment creation failed")
		user.apartmentId = apartment.apartmentId
		authService.updateUser(user)
		return apartment
	}
}