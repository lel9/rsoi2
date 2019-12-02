package ru.bmstu.testsystem.exams.model

import ru.bmstu.testsystem.exams.domain.Question
import ru.bmstu.testsystem.exams.domain.QuestionType

data class QuestionDataIn (
    var questionText: String? = null,
    var type: QuestionType? = null,
    var variants: List<String>? = null,
    var correctVariants: List<Int>? = null,
    var correctInputAnswer: String? = null
) {
    constructor(question: Question) : this(question.questionText, question.type,
        question.variants, question.correctVariantsId, question.correctInputAnswer)

    fun toQuestion(id: Int) : Question {
        questionText?: throw IllegalArgumentException("Текст вопроса не задан")
        type?: throw IllegalArgumentException("Тип вопроса не задан")
        if (variants != null && correctInputAnswer != null)
            throw IllegalArgumentException("Нельзя задать одновременно и варианты ответа, и правильный ответ в виде строки")
        if (variants == null && correctVariants != null)
            throw IllegalArgumentException("Не заданы варианты ответа")
        if ((correctVariants == null && correctInputAnswer == null))
            throw IllegalArgumentException("Не указаны правильные ответы")
        return Question(id, questionText!!, type!!, variants, correctVariants, correctInputAnswer)
    }
}
