package ru.bmstu.testsystem.result.model

import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class UserAnswers (

        @field:NotNull(message="Список вопросов должен быть задан")
        @field:NotEmpty(message="Список вопросов не может быть пуст")
        var questions: List<QuestionDataWithAnsOut?>? = null,

        @field:NotNull(message="Список ответов должен быть задан")
        @field:NotEmpty(message="Список ответов не может быть пуст")
        var list: List<UserAnswer?>? = null
)

data class UserAnswer (
        @field:NotNull(message="id вопроса должен быть задан")
        @field:NotEmpty(message="id вопроса не может быть пуст")
        var questionId: Int? = null,

        var checkedVariants: MutableList<Int>? = null,

        var inputAnswer: String? = null
)
