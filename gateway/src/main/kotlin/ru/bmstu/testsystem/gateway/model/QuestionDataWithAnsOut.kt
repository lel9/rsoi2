package ru.bmstu.testsystem.gateway.model

enum class QuestionType {
    SINGLE_ANSWER,
    MULTIPLE_ANSWER,
    NO_ANSWER;
}

data class QuestionDataWithAnsOut (
    val id: Int? = null,
    var questionText: String? = null,
    var type: QuestionType? = null,
    var variants: List<String>? = null,
    var correctVariants: List<Int>? = null,
    var correctInputAnswer: String? = null
)
