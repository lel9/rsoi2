package ru.bmstu.testsystem.exams.domain


data class Question (

        val id: Int? = null,

        val questionText: String? = null,

        val type: QuestionType? = null,

        // если есть варианты
        val variants: List<String>? = null,

        val correctVariantsId: List<Int>? = null,

        // если нет вариантов
        val correctInputAnswer: String? = null

)
