package codes.keshav.home.management.dto

import codes.keshav.home.management.dto.response.Meta
import codes.keshav.home.management.dto.response.UserDataResponse
import java.util.*

data class UserData(
	val email: String,
	var password: String,
	val firstName: String,
	val lastName: String,
	var apartmentId: UUID?,
	val meta: Meta?
) {
	fun toResponse(): UserDataResponse {
		return UserDataResponse(email, firstName, lastName, meta, apartmentId)
	}
}