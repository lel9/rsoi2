package ru.bmstu.testsystem.users.model

import ru.bmstu.testsystem.users.domain.User
import java.util.*

data class UserData (
    var id: String,
    var username: String,
    var email: String,
    var firstName: String? = null,
    var lastName: String? = null,
    var birthday: Date? = null
) {
    constructor(user: User): this(user.id.toString(), user.username, user.email,
        user.person.firstName, user.person.lastName, user.person.birthday)
}
