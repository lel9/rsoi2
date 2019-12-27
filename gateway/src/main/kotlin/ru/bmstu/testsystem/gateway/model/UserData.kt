package ru.bmstu.testsystem.gateway.model

import java.util.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class UserData (
    @field:NotNull(message="id пользователя должно быть задано")
    @field:NotEmpty(message="id пользователя не может быть пусто")
    var id: String,

    @field:NotNull(message="Имя пользователя должно быть задано")
    @field:NotEmpty(message="Имя пользователя не может быть пусто")
    var username: String? = null,

    @field:NotNull(message="Email должен быть задан")
    @field:NotEmpty(message="Email не может быть пуст")
    var email: String? = null,

    var firstName: String? = null,
    var lastName: String? = null,
    var birthday: Date? = null
) {
    constructor() : this("", "", "")
}
