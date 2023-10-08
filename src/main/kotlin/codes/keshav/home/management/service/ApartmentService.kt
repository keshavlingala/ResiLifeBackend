package codes.keshav.home.management.service

import codes.keshav.home.management.dto.EqualParam
import codes.keshav.home.management.dto.request.Apartment
import codes.keshav.home.management.dto.request.ApartmentRequest
import codes.keshav.home.management.dto.request.CreateAptRequest
import codes.keshav.home.management.dto.request.Payload
import codes.keshav.home.management.exceptions.DataConversionException
import codes.keshav.home.management.retrofit.Postgrest
import codes.keshav.home.management.socket.GroupSocketHandler
import codes.keshav.home.management.socket.UserSocketHandler
import codes.keshav.home.management.utils.validatedExecute
import org.springframework.stereotype.Service
import java.time.Instant
import java.util.*

@Service
class ApartmentService(
	val postgrest: Postgrest,
	val authService: AuthService,
	val userSocketHandler: UserSocketHandler,
	val groupSocketHandler: GroupSocketHandler
) {

	fun createApt(request: ApartmentRequest): Apartment {
		val user = authService.getCurrentUser()
		if (user.apartmentId != null) {
			throw DataConversionException("User already has an apartment")
		}
		val apartment = postgrest.createApt(
			CreateAptRequest(
				name = request.name,
				members = listOf(user.email),
				payload = Payload(
					owner = user.email,
				),
				createDate = Instant.now()
			)
		).validatedExecute().firstOrNull() ?: throw DataConversionException("Apartment creation failed")
		user.apartmentId = apartment.apartmentId
		authService.updateUser(user)
		userSocketHandler.updateUserData(user)
		groupSocketHandler.updateApartment(apartment)
		return apartment
	}

	fun getApt(): Apartment {
		val user = this.authService.getCurrentUser()
		if (user.apartmentId == null) {
			throw DataConversionException("User does not have an apartment")
		}
		return postgrest.getApt(EqualParam(user.apartmentId.toString())).validatedExecute().firstOrNull()
			?: throw DataConversionException("Apartment not found")
	}

	fun joinApt(aptId: UUID?): Apartment {
		val user = this.authService.getCurrentUser()
		val apartment = postgrest.getApt(EqualParam(aptId.toString())).validatedExecute().firstOrNull()
			?: throw DataConversionException("Apartment not found")
		user.apartmentId = apartment.apartmentId
		apartment.members?.add(user.email)
		postgrest.updateApt(
			EqualParam(apartment.apartmentId.toString()),
			apartment
		).validatedExecute()
		authService.updateUser(user)
		userSocketHandler.updateUserData(user)
		groupSocketHandler.updateApartment(apartment)
		return apartment
	}
}
