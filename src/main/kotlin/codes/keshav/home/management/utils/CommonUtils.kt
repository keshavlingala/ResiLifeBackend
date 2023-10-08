package codes.keshav.home.management.utils

import codes.keshav.home.management.exceptions.DataConversionException
import codes.keshav.home.management.exceptions.DataException
import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import retrofit2.Call

fun <T> Call<T>.validatedExecute(): T {
	val response = this.execute()
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

object Utils {
	val logger = LoggerFactory.getLogger(Utils::class.java)
	lateinit var objectMapper: ObjectMapper
	inline fun <reified T> String.getObject(): T? {
		return try {
			objectMapper.readValue(this, T::class.java)
		} catch (e: Exception) {
			logger.error("Failed to convert message to object", e)
			null
		}
	}
}

fun Any.toJsonString(): String {
	return try {
		Utils.objectMapper.writeValueAsString(this)
	} catch (e: Exception) {
		throw DataConversionException("Failed to convert object to JSON string")
	}
}

