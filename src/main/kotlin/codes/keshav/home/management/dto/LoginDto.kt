package codes.keshav.home.management.dto

import java.beans.ConstructorProperties

data class LoginDto
@ConstructorProperties("email", "password")
constructor(val email: String, var password: String)