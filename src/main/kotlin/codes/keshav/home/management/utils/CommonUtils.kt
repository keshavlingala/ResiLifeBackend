package codes.keshav.home.management.utils

import codes.keshav.home.management.exceptions.DataException
import retrofit2.Call

fun <T> Call<T>.validatedExecute(): T {
	val response = this.execute()
	if (response.code() == 409) {
		throw DataException("User already exists!")
	}
	if (response.isSuccessful.not()) {
		throw DataException("Api Call not Successful! ${response.code()} ${response.message()}")
	}
	return response.body() ?: throw DataException("Data is null!")
}