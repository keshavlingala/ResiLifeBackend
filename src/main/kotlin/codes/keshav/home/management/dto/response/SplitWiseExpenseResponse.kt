package codes.keshav.home.management.dto.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class SplitWiseExpenseResponse(
	val expenses: List<SplitWiseExpense>?
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class SplitWiseExpense(
	val id: Long?,
	val group_id: Long?,
	val friendship_id: Long?,
	val expense_bundle_id: Long?,
	val description: String?,
	val repeats: Boolean?,
	val repeat_interval: Long?,
	val email_reminder: Boolean?,
	val email_reminder_in_advance: Long?,
	val next_repeat: String?,
	val details: String?,
	val comments_count: Long?,
	val payment: Boolean?,
	val creation_method: String?,
	val transaction_method: String?,
	val transaction_confirmed: Boolean?,
	val transaction_id: Long?,
	val transaction_status: String?,
	val cost: String?,
	val currency_code: String?,
	val repayments: List<SplitWiseRepayment>?,
	val date: String?,
	val created_at: String?,
	val created_by: SplitWiseExpenseUpdateUser?,
	val updated_at: String?,
	val updated_by: SplitWiseExpenseUpdateUser?,
	val deleted_at: String?,
	val deleted_by: SplitWiseExpenseUpdateUser?,
	val category: SplitWiseCategory?,
	val receipt: SplitWiseReceipt?,
	val users: List<SplitWiseExpenseUser>?
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class SplitWiseRepayment(
	val from: Long?,
	val to: Long?,
	val amount: String?
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class SplitWiseExpenseUpdateUser(
	val id: Long?,
	val first_name: String?,
	val last_name: String?,
	val picture: SplitWisePicture?,
	val custom_picture: Boolean?
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class SplitWiseCategory(
	val id: Long?,
	val name: String?
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class SplitWiseReceipt(
	val large: String?,
	val original: String?
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class SplitWiseExpenseUser(
	val user: SplitWiseExpenseUpdateUser?,
	val user_id: Long?,
	val paid_share: String?,
	val owed_share: String?,
	val net_balance: String?
)

