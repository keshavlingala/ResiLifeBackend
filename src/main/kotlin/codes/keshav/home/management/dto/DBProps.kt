package codes.keshav.home.management.dto

data class EqualParam(
				val param: String
) {
	override fun toString(): String {
		return "eq.$param"
	}
}