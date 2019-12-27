package ru.bmstu.testsystem.users.model

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class RegistrationData (
    @field:NotNull(message="Имя пользователя должно быть задано")
    @field:NotEmpty(message="Имя пользователя не может быть пусто")
    var username: String?,

    @field:NotNull(message="Email должен быть задан")
    @field:NotEmpty(message="Email не может быть пуст")
    var email: String?
)
