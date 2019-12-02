package ru.bmstu.testsystem.gateway.model

data class QuestionDataOut (
    val id: Int? = null,
    var questionText: String? = null,
    var type: QuestionType? = null,
    var variants: List<String>? = null
)
