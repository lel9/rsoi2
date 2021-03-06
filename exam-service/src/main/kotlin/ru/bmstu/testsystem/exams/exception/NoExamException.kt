package ru.bmstu.testsystem.exams.exception

import ru.bmstu.testsystem.exams.model.Error
import ru.bmstu.testsystem.exams.model.getErrorInfo

class NoExamException : AppException() {

    override val message: String?
        get() = getErrorInfo(Error.noExam)
}
