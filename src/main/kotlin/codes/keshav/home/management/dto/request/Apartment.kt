package codes.keshav.home.management.dto.request

import java.time.Instant
import java.util.*

data class ApartmentRequest(
	val name: String
)

data class Apartment(
	val apartmentId: UUID,
	val name: String,
	val picture: String?,
	var members: MutableList<String>?, // List of user emails
	val payload: Payload?,
	val createDate: Instant?
)

data class Payload(
	val owner: String,
	val notes: List<Note>? = null
)

data class Note(
	val title: String,
	val content: String,
	val author: String,
	val createDate: Instant = Instant.now()
)