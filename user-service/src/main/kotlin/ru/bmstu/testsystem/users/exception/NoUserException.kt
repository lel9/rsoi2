package ru.bmstu.testsystem.users.exception

import ru.bmstu.testsystem.users.model.Error
import ru.bmstu.testsystem.users.model.getErrorInfo

class NoUserException : AppException() {
    override val message: String?
        get() = getErrorInfo(Error.noUser)
}
