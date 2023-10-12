package codes.keshav.home.management.retrofit

import codes.keshav.home.management.dto.Bearer
import codes.keshav.home.management.dto.response.SplitWiseExpenseResponse
import codes.keshav.home.management.dto.response.SplitWiseUserResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header

interface SplitWiseApi {

	@GET("get_current_user")
	fun getCurrentUser(
		@Header("Authorization") authToken: String
	): Call<SplitWiseUserResponse>

	@GET("get_expenses")
	fun getExpenses(
		@Header("Authorization") authToken: Bearer
	): Call<SplitWiseExpenseResponse>
}
