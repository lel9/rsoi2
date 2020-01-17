package ru.bmstu.testsystem.model

data class ExamDataIn (
        var name: String? = null,
        var description: String? = null,
        var questionIns: MutableList<QuestionDataIn>? = null
) {
    constructor() : this("", "", arrayListOf())
}
