package codes.keshav.home.management.dto.response

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

@JsonIgnoreProperties(ignoreUnknown = true)
data class SplitWiseUserResponse(
	val user: SplitWiseUser?
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class SplitWiseUser(
	val id: Int?,
	val first_name: String?,
	val last_name: String?,
	val picture: SplitWisePicture?,
	val custom_picture: Boolean?,
	val email: String?,
	val registration_status: String?,
	val force_refresh_at: String?,
	val locale: String?,
	val country_code: String?,
	val date_format: String?,
	val default_currency: String?,
	val default_group_id: Int?,
	val notifications_read: String?,
	val notifications_count: Int?,
	val notifications: SplitWiseNotifications?
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class SplitWisePicture(
	val small: String?,
	val medium: String?,
	val large: String?
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class SplitWiseNotifications(
	val added_as_friend: Boolean?,
	val added_to_group: Boolean?,
	val expense_added: Boolean?,
	val expense_updated: Boolean?,
	val bills: Boolean?,
	val payments: Boolean?,
	val monthly_summary: Boolean?,
	val announcements: Boolean?
)
