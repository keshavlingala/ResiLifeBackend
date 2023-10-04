package codes.keshav.home.management.config

import codes.keshav.home.management.properties.AppProperties
import codes.keshav.home.management.retrofit.Postgrest
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.nimbusds.jose.jwk.source.ImmutableSecret
import okhttp3.OkHttpClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import javax.crypto.spec.SecretKeySpec

@Configuration
class AppConfig(
	val appProperties: AppProperties
) {

	@Bean
	fun objectMapper(): ObjectMapper {
		return ObjectMapper()
			.registerModule(JavaTimeModule())
			.registerModule(
				KotlinModule.Builder()
					.withReflectionCacheSize(512)
					.configure(KotlinFeature.NullToEmptyCollection, false)
					.configure(KotlinFeature.NullToEmptyMap, false)
					.configure(KotlinFeature.NullIsSameAsDefault, false)
					.configure(KotlinFeature.SingletonSupport, false)
					.configure(KotlinFeature.StrictNullChecks, false)
					.build()
			)

	}

	@Bean
	fun postgRest(): Postgrest {
		return Retrofit.Builder()
			.baseUrl(appProperties.postgRest.url)
			.addConverterFactory(JacksonConverterFactory.create(objectMapper()))
			.client(OkHttpClient.Builder().build())
			.build()
			.create(Postgrest::class.java)

	}

	@Bean
	fun authenticationManager(): AuthenticationManager {
		return AuthenticationManager { authentication ->
			authentication
		}
	}

	private val jwtKey = "secret123"
	private val secret = SecretKeySpec(jwtKey.toByteArray(), "HmacSHA256")

	@Bean
	fun encoder() = NimbusJwtEncoder(ImmutableSecret(secret))

	@Bean
	fun decoder() = NimbusJwtDecoder.withSecretKey(secret).build()
}