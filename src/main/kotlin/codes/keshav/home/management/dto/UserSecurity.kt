package codes.keshav.home.management.dto

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class UserSecurity(
	val id: Int,
	val email: String,
	private val password: String,
) : UserDetails {
	override fun getAuthorities() = emptyList<GrantedAuthority>()
	override fun getPassword() = password
	override fun getUsername() = email
	override fun isAccountNonExpired() = true
	override fun isAccountNonLocked() = true
	override fun isCredentialsNonExpired() = true
	override fun isEnabled() = true
}