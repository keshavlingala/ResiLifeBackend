package codes.keshav.home.management.dto.response

import java.util.*

data class UserDataResponse(
	val email: String,
	val firstName: String?,
	val lastName: String?,
	val meta: Meta?,
	val apartmentId: UUID?
)

data class Meta(
	val splitwiseApiKey: String?,
	val canvasApiKey: String?,
	val picture: String?
)
