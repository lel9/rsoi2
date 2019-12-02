package ru.bmstu.testsystem.exams.model

enum class Error  {
    noExam,
    deletedExam
}

fun getErrorInfo(error: Error) : String =
    when(error) {
        Error.noExam -> "Такого теста не существует"
        Error.deletedExam -> "Тест был удалён"
    }
