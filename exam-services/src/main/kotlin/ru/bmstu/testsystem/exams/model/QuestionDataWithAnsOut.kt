package ru.bmstu.testsystem.exams.model

import ru.bmstu.testsystem.exams.domain.Question
import ru.bmstu.testsystem.exams.domain.QuestionType


data class QuestionDataWithAnsOut (
    val id: Int? = null,
    var questionText: String? = null,
    var type: QuestionType? = null,
    var variants: List<String>? = null,
    var correctVariants: List<Int>? = null,
    var correctInputAnswer: String? = null
) {
    constructor(question: Question) : this(question.id, question.questionText, question.type,
        question.variants, question.correctVariantsId, question.correctInputAnswer)
}
