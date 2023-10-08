package codes.keshav.home.management.dto.request

import java.time.Instant

class CreateAptRequest(
	val name: String,
	val members: List<String>,
	val payload: Payload? = null,
	val createDate: Instant
)
