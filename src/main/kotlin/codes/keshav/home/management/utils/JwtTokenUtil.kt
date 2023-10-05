package codes.keshav.home.management.utils

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.exceptions.JWTVerificationException
import org.springframework.stereotype.Component
import java.util.*


@Component
class JwtTokenUtil(
) {
	val algorithm: Algorithm = Algorithm.HMAC256("supersuperSecretKey")
	val issuer = "homemanagementsystem"
	fun generateToken(email: String): String {

		val currentTimeMillis = System.currentTimeMillis()
		val expiryTime = Date(currentTimeMillis + 15 * 24 * 60 * 60 * 1000)

		return JWT.create()
			.withIssuer(issuer)
			.withClaim("email", email)
			.withExpiresAt(expiryTime)
			.sign(algorithm)
	}

	fun verifyJWT(token: String): Boolean {
		return try {
			val verifier = JWT.require(algorithm)
				.withIssuer(issuer)
				.build()
			verifier.verify(token)
			true
		} catch (e: JWTVerificationException) {
			false
		} catch (e: JWTDecodeException) {
			false
		}
	}

	fun getUserNameFromToken(token: String): String {
		return JWT.decode(token).getClaim("email").asString()
	}
}