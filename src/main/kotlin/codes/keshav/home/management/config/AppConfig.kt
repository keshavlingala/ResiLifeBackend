package codes.keshav.home.management.config

import codes.keshav.home.management.properties.AppProperties
import codes.keshav.home.management.retrofit.Postgrest
import codes.keshav.home.management.service.JwtFilter
import codes.keshav.home.management.utils.JwtTokenUtil
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinFeature
import com.fasterxml.jackson.module.kotlin.KotlinModule
import okhttp3.OkHttpClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.UrlBasedCorsConfigurationSource
import org.springframework.web.filter.CorsFilter
import retrofit2.Retrofit
import retrofit2.converter.jackson.JacksonConverterFactory
import java.time.Instant


@Configuration
class AppConfig(
	val appProperties: AppProperties,
	val jwtTokenUtil: JwtTokenUtil
) {

	@Bean
	fun objectMapper(): ObjectMapper {
		val javaTimeModule = JavaTimeModule()
		javaTimeModule.addDeserializer(
			Instant::class.java,
			object : JsonDeserializer<Instant>() {
				override fun deserialize(p: JsonParser, ctxt: DeserializationContext): Instant {
					val timestamp = p.text
					return Instant.parse(timestamp.substring(0, 26) + "Z")
				}
			}
		)

		return ObjectMapper()
			.registerModule(javaTimeModule)
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
	fun passwordEncoder(): PasswordEncoder {
		return BCryptPasswordEncoder()
	}

	@Bean
	@Throws(Exception::class)
	fun filterChain(http: HttpSecurity): SecurityFilterChain {
		http
			.csrf { it.disable() }
			.cors { it.disable() }
			.authorizeHttpRequests { requests ->
				requests
					.requestMatchers("/auth/**").permitAll()
					.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
					.anyRequest().authenticated()
			}
			.sessionManagement { session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
			.addFilterBefore(JwtFilter(postgRest(), jwtTokenUtil), UsernamePasswordAuthenticationFilter::class.java)
		return http.build()
	}

	@Bean
	fun corsFilter(): CorsFilter {
		val source = UrlBasedCorsConfigurationSource()
		val config = CorsConfiguration()
		config.allowCredentials = false
		config.addAllowedOrigin("*")
		config.addAllowedHeader("*")
		config.addAllowedMethod("GET")
		config.addAllowedMethod("POST")
		config.addAllowedMethod("PUT")
		config.addAllowedMethod("DELETE")
		source.registerCorsConfiguration("/**", config)
		return CorsFilter(source)
	}

}