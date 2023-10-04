package codes.keshav.home.management.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("api")
data class AppProperties(
				val postgRest: ApiProp
)

data class ApiProp(
				val url: String
)