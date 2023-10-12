package codes.keshav.home.management.service

import codes.keshav.home.management.dto.Bearer
import codes.keshav.home.management.dto.response.SplitWiseExpenseResponse
import codes.keshav.home.management.exceptions.NoApiKeyException
import codes.keshav.home.management.retrofit.SplitWiseApi
import codes.keshav.home.management.utils.validatedExecute
import org.springframework.stereotype.Service

@Service
class SplitWiseService(
	val splitWiseApi: SplitWiseApi,
	val authService: AuthService
) {
	fun getSplitwiseData() {

	}

	fun getExpenses(): SplitWiseExpenseResponse {
		val user = authService.getCurrentUser()
		val token = user.meta?.splitwiseApiKey ?: throw NoApiKeyException("Splitwise API key not found")
		return splitWiseApi.getExpenses(Bearer(token)).validatedExecute()
	}
}