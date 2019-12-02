package ru.bmstu.testsystem.users.domain

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "person_data")
data class Person (
        var firstName: String? = null,

        var lastName: String? = null,

        var birthday: Date? = null
) {
    @GeneratedValue
    @Id
    var id: UUID = UUID.randomUUID()
}
