package codes.keshav.home.management.dto.response

data class UserDataResponse(
	val email: String,
	val firstName: String?,
	val lastName: String?,
	val meta: Any?
)