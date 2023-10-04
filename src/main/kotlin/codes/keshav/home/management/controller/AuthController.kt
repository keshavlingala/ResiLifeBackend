package codes.keshav.home.management.controller

import codes.keshav.home.management.dto.EqualParam
import codes.keshav.home.management.dto.JwtResponse
import codes.keshav.home.management.dto.SignRequest
import codes.keshav.home.management.retrofit.Postgrest
import codes.keshav.home.management.utils.JwtTokenUtil
import codes.keshav.home.management.utils.validatedExecute
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("auth")
class AuthController(
	val postgrest: Postgrest,
	val passwordEncoder: BCryptPasswordEncoder,
	val jwtTokenUtil: JwtTokenUtil
) {
	@PostMapping("signup")
	fun signup(
		@RequestBody user: SignRequest
	): JwtResponse {
		println("Signup Controller Reached!")
		user.password = passwordEncoder.encode(user.password)
		val response = postgrest.addUser(user).validatedExecute().firstOrNull()
			?: throw Exception("User not created!")
		return JwtResponse(jwtTokenUtil.generateToken(response.email))
	}

	@PostMapping("login")
	fun login(
		@RequestBody request: SignRequest
	): JwtResponse {
		println("Login Controller Reached!")
		val user = postgrest.getUserByName(EqualParam(request.email)).validatedExecute().firstOrNull()
		println(user)
		if (user != null) {
			if (passwordEncoder.matches(request.password, user.password)) {
				val res = JwtResponse(jwtTokenUtil.generateToken(user.email))
				return res
			} else {
				throw Exception("Invalid Password")
			}
		}
		throw Exception("User not found")
	}
}
