package codes.keshav.home.management.dto

import codes.keshav.home.management.dto.response.UserDataResponse

data class UserData(
	val email: String,
	var password: String,
	val firstName: String,
	val lastName: String,
	val meta: Any?
) {
	fun toResponse(): UserDataResponse {
		return UserDataResponse(email, firstName, lastName, meta)
	}
}