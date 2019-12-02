package ru.bmstu.testsystem.gateway.model

data class UserAnswers (

        var questions: List<QuestionDataWithAnsOut?>? = null,

        var list: List<UserAnswer?>? = null
)

data class UserAnswer (
        var questionId: Int? = null,

        var checkedVariants: MutableList<Int>? = null,

        var inputAnswer: String? = null
)
