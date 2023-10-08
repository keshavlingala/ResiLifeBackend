package codes.keshav.home.management.retrofit

import codes.keshav.home.management.dto.EqualParam
import codes.keshav.home.management.dto.UserData
import codes.keshav.home.management.dto.request.Apartment
import codes.keshav.home.management.dto.request.CreateAptRequest
import codes.keshav.home.management.dto.response.UserDataResponse
import codes.keshav.home.management.utils.MERGE_DUPLICATE
import codes.keshav.home.management.utils.RETURN_OBJECT
import retrofit2.Call
import retrofit2.http.*

interface Postgrest {
	@GET("users")
	fun getUser(
		@Query("email") email: EqualParam
	): Call<List<UserData>>

	@Headers(RETURN_OBJECT)
	@POST("users")
	fun addUser(
		@Body body: UserData
	): Call<List<UserData>>

	@Headers(RETURN_OBJECT)
	@POST("apartments")
	fun createApt(
		@Body apartment: CreateAptRequest
	): Call<List<Apartment>>

	@Headers(RETURN_OBJECT, MERGE_DUPLICATE)
	@PATCH("users")
	fun updateUser(
		@Query("email") email: EqualParam,
		@Body user: UserDataResponse
	): Call<List<UserData>>

	@Headers(RETURN_OBJECT, MERGE_DUPLICATE)
	@PATCH("apartments")
	fun updateApt(
		@Query("apartmentId") apartmentId: EqualParam,
		@Body apartment: Apartment
	): Call<List<Apartment>>

	@GET("apartments")
	fun getApt(
		@Query("apartmentId") equalParam: EqualParam
	): Call<List<Apartment>>
}

