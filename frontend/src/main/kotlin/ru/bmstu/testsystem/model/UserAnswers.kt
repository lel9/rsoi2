package ru.bmstu.testsystem.model

data class UserAnswers (
        var list: List<UserAnswer?>? = null,
        var userId: String? = null
)

data class UserAnswer (
        var questionId: Int? = null,

        var checkedVariants: MutableList<Int>? = null,

        var inputAnswer: String? = null
)
