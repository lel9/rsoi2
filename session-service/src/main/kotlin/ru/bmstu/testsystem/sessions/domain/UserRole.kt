package ru.bmstu.testsystem.sessions.domain

import org.springframework.security.core.GrantedAuthority

enum class UserRole constructor(private val role: String) : GrantedAuthority {
    USER("ROLE_USER");

    override fun getAuthority(): String {
        return this.role
    }

}