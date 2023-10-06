package codes.keshav.home.management.dto.response

import java.util.*

data class UserDataResponse(
	val email: String,
	val firstName: String?,
	val lastName: String?,
	val meta: Any?,
	val apartmentId: UUID?
)