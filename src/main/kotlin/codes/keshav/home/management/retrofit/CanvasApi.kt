package codes.keshav.home.management.retrofit

import codes.keshav.home.management.dto.Bearer
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy
import com.fasterxml.jackson.databind.annotation.JsonNaming
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.QueryMap

interface CanvasApi {
	@GET("courses?type=student&enrollment_state=active")
	fun getCourses(
		@Header("Authorization") authToken: Bearer
	): Call<List<ActiveCourse>>

	@GET("calendar_events")
	fun getCalendarEvents(
		@Header("Authorization") authToken: Bearer,
		@QueryMap params: Map<String, String>
	): Call<List<Any>>
}


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonNaming(SnakeCaseStrategy::class)
data class ActiveCourse(
	val id: Int,
	val name: String,
	val accountId: Int,
	val createdAt: String?,
	val courseCode: String,
	val originalName: String?,
	val endAt: String?,
)