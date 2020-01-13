package ru.bmstu.testsystem.sessions.domain

import java.io.Serializable
import java.util.*
import javax.persistence.*
import javax.persistence.GeneratedValue


@Entity
@Table(name = "users")
class User(var username: String, var password_hash: String): Serializable {

    @Enumerated(EnumType.STRING)
    val role: UserRole = UserRole.USER

    @GeneratedValue
    @Id
    var id: UUID = UUID.randomUUID()

    companion object {
        private const val serialVersionUID = 20180617104400L
    }

}

