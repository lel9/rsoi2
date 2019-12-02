package ru.bmstu.testsystem.users.model

enum class Error  {
    noUser,
    badData,
    userAlreadyExists
}

fun getErrorInfo(error: Error) : String =
    when(error) {
        Error.noUser -> "Такого пользователя не существует"
        Error.badData -> "bad data"
        Error.userAlreadyExists -> "Пользователь с таким именем уже существует"
    }
