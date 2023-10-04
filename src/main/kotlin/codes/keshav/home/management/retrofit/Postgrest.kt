package codes.keshav.home.management.retrofit

import codes.keshav.home.management.dto.EqualParam
import codes.keshav.home.management.dto.SignRequest
import codes.keshav.home.management.dto.UserSecurity
import codes.keshav.home.management.utils.RETURN_OBJECT
import retrofit2.Call
import retrofit2.http.*

interface Postgrest {
	@GET("users")
	fun getUser(
		@Query("id") id: EqualParam
	): Call<List<UserSecurity>>

	@GET("users")
	fun getUserByName(
		@Query("email") email: EqualParam
	): Call<List<UserSecurity>>

	@Headers(RETURN_OBJECT)
	@POST("users")
	fun addUser(
		@Body body: SignRequest
	): Call<List<UserSecurity>>

}

