package ru.bmstu.testsystem.users.domain

import java.util.*
import javax.persistence.*
import javax.persistence.JoinColumn
import javax.persistence.GeneratedValue



@Entity
@Table(name = "users")
data class User (

        var username: String,

        var email: String

) {
    @GeneratedValue
    @Id
    var id: UUID = UUID.randomUUID()

    @OneToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @JoinColumn(name="person_id")
    var person: Person = Person()

}

