package codes.keshav.home.management.auth

import codes.keshav.home.management.service.UserDetailsService
import codes.keshav.home.management.utils.JwtTokenUtil
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter

@Configuration
class SecurityConfig(
	private val userDetailsService: UserDetailsService,
	private val jwtTokenUtil: JwtTokenUtil,
	private val authenticationManager: AuthenticationManager
) {

	private fun authManager(http: HttpSecurity): AuthenticationManager {
		val authenticationManagerBuilder = http.getSharedObject(
			AuthenticationManagerBuilder::class.java
		)
		authenticationManagerBuilder.userDetailsService(userDetailsService)
		return authenticationManagerBuilder.build()
	}

	@Bean
	fun filterChain(http: HttpSecurity): SecurityFilterChain {
		val authenticationManager = authManager(http)
		http
			.csrf { it.disable() }
			.cors { it.disable() }
			.authorizeHttpRequests { auth ->
				auth.requestMatchers("/auth/**").permitAll()
					.anyRequest().authenticated()
			}
			.authenticationManager(authenticationManager)
			.sessionManagement { session ->
				session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			}
			.addFilterBefore(
				JwtAuthenticationFilter(jwtTokenUtil, authenticationManager),
				BasicAuthenticationFilter::class.java
			)
		return http.build()
	}

	@Bean
	open fun bCryptPasswordEncoder(): BCryptPasswordEncoder {
		return BCryptPasswordEncoder()
	}
}