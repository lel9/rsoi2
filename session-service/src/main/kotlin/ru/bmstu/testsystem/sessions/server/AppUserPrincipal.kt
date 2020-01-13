package ru.bmstu.testsystem.sessions.server

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import ru.bmstu.testsystem.sessions.domain.User


class AppUserPrincipal(private val user: User) : UserDetails {

    override fun getUsername(): String {
        return user.username
    }

    override fun getPassword(): String {
        return user.password_hash
    }

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return listOf<GrantedAuthority>(user.role)
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

}