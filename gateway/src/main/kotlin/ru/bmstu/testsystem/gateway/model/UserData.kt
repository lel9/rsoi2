package ru.bmstu.testsystem.gateway.model

import java.util.*

data class UserData (
    var id: String,
    var username: String,
    var email: String,
    var firstName: String? = null,
    var lastName: String? = null,
    var birthday: Date? = null
)
