package ru.bmstu.testsystem.result.model

import ru.bmstu.testsystem.result.domain.QuestionType


data class QuestionDataWithAnsOut (
    val id: Int? = null,
    var questionText: String? = null,
    var type: QuestionType? = null,
    var variants: List<String>? = null,
    var correctVariants: List<Int>? = null,
    var correctInputAnswer: String? = null
)
