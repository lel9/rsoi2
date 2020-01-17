package ru.bmstu.testsystem.model

enum class QuestionType {
    SINGLE_ANSWER,
    MULTIPLE_ANSWER,
    NO_ANSWER;
}

data class QuestionDataIn (
    var questionText: String? = null,
    var type: QuestionType? = null,
    var variants: List<String>? = null,
    var correctVariants: List<Int>? = null,
    var correctInputAnswer: String? = null
)
