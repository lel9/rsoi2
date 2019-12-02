package ru.bmstu.testsystem.gateway.model

import java.util.*

data class ExamDataWithAnsOut (
    var id: UUID,
    var name: String,
    var passes: Int,
    var description: String,
    var questionIns: List<QuestionDataWithAnsOut>
)
