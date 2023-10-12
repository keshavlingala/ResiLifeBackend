package codes.keshav.home.management.dto

data class EqualParam(
	val param: String
) {
	override fun toString(): String {
		return "eq.$param"
	}
}

data class Bearer(
	val token: String
) {
	override fun toString(): String {
		return "Bearer $token"
	}
}