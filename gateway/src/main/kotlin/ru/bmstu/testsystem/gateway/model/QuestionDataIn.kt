package ru.bmstu.testsystem.gateway.model

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
)
