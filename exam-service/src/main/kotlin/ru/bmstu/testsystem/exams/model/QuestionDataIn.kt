package ru.bmstu.testsystem.exams.model

import ru.bmstu.testsystem.exams.domain.Question
import ru.bmstu.testsystem.exams.domain.QuestionType
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class QuestionDataIn (
    @field:NotNull(message="Тип вопроса должен быть задан")
    @field:NotEmpty(message="Тип вопроса не может быть пуст")
    var questionText: String? = null,

    @field:NotNull(message="Тип вопроса должен быть задан")
    var type: QuestionType? = null,

    var variants: List<String>? = null,
    var correctVariants: List<Int>? = null,
    var correctInputAnswer: String? = null
) {
    constructor(question: Question) : this(question.questionText, question.type,
        question.variants, question.correctVariantsId, question.correctInputAnswer)

    fun toQuestion(id: Int) : Question {
        if (variants != null && correctInputAnswer != null)
            throw IllegalArgumentException("Нельзя задать одновременно и варианты ответа, и правильный ответ в виде строки")
        if (variants == null && correctVariants != null)
            throw IllegalArgumentException("Не заданы варианты ответа")
        if ((correctVariants == null && correctInputAnswer == null))
            throw IllegalArgumentException("Не указаны правильные ответы")
        return Question(id, questionText!!, type!!, variants, correctVariants, correctInputAnswer)
    }
}
