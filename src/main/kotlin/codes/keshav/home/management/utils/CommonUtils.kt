package codes.keshav.home.management.utils

import codes.keshav.home.management.exceptions.DataException
import retrofit2.Call

fun <T> Call<T>.validatedExecute(): T {
	val response = this.execute()
	if (response.code() == 409) {
		throw DataException("User already exists!")
	}
	if (response.code() == 400) {
		throw DataException("Bad Request!")
	}
	if (response.isSuccessful.not()) {
		println(response)
		throw DataException("Api Call not Successful! ${response.code()} ${response.message()}")
	}
	return response.body() ?: throw DataException("Data is null!")
}

fun <T> Call<T>.fetchOrNull(): T? {
	val response = this.execute()
	if (response.isSuccessful.not()) {
		println(response)
		return null
	}
	return response.body()
}