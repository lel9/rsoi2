package ru.bmstu.testsystem.gateway.model
import java.util.*

data class ExamDataIn (
        var name: String,
        var description: String,
        var questionIns: MutableList<QuestionDataIn>
)
