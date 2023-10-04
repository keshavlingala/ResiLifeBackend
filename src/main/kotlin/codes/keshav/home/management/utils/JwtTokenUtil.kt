package codes.keshav.home.management.utils

import org.springframework.security.oauth2.jwt.*
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.temporal.ChronoUnit


@Component
class JwtTokenUtil(
	private val jwtEncoder: JwtEncoder,
	private val jwtDecoder: JwtDecoder,
) {

	private val expiration = 6000000L

	fun generateToken(email: String): String {
		val claims = JwtClaimsSet.builder()
			.issuedAt(Instant.now())
			.expiresAt(Instant.now().plus(30L, ChronoUnit.DAYS))
			.subject(email)
			.claim("email", email)
			.build()
		return jwtEncoder.encode(JwtEncoderParameters.from(claims)).tokenValue
	}

	fun getJwt(token: String): Jwt? {
		return jwtDecoder.decode(token)
	}

	fun getEmail(token: String): String? {
		return try {
			val jwt = jwtDecoder.decode(token)
			return jwt.claims["email"] as String
		} catch (e: Exception) {
			null
		}
	}

	fun isTokenValid(token: String): Boolean {
		val jwt = getJwt(token)
		return jwt != null && jwt.expiresAt?.isAfter(Instant.now()) ?: false
	}
}