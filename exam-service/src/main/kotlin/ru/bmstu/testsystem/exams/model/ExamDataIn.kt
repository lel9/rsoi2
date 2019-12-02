package ru.bmstu.testsystem.exams.model

import ru.bmstu.testsystem.exams.domain.Exam
import java.sql.Date
import java.util.*

data class ExamDataIn (
        var name: String,
        var description: String,
        var questionIns: MutableList<QuestionDataIn>
) {
    fun toExam() : Exam {
        val questionList = questionIns.mapIndexed { i, q -> q.toQuestion(i) }
        return Exam(name, description, Date(System.currentTimeMillis()), questionList)
    }
}
