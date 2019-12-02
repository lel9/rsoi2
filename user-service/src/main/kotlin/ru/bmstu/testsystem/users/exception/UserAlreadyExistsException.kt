package ru.bmstu.testsystem.users.exception

import ru.bmstu.testsystem.users.model.Error
import ru.bmstu.testsystem.users.model.getErrorInfo

class UserAlreadyExistsException : AppException() {
    override val message: String?
        get() = getErrorInfo(Error.userAlreadyExists)
}
