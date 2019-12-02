package ru.bmstu.testsystem.gateway.model


data class QuestionDataIn (
    var questionText: String? = null,
    var type: QuestionType? = null,
    var variants: List<String>? = null,
    var correctVariants: List<Int>? = null,
    var correctInputAnswer: String? = null
)
