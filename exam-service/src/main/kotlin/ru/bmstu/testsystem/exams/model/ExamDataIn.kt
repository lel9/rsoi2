package ru.bmstu.testsystem.exams.model

import ru.bmstu.testsystem.exams.domain.Exam
import java.sql.Date
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class ExamDataIn (
    @field:NotNull(message="Название теста должен быть задано")
    @field:NotEmpty(message="Название теста не может быть пусто")
    var name: String?,

    @field:NotNull(message="Описание должен быть задано")
    @field:NotEmpty(message="Описание не может быть пусто")
    var description: String?,

    @field:NotNull(message="Список вопросов должен быть задан")
    @field:NotEmpty(message="Список вопросов не может быть пуст")
    var questionIns: MutableList<QuestionDataIn>?
) {
    fun toExam() : Exam {
        val questionList = questionIns!!.mapIndexed { i, q -> q.toQuestion(i) }
        return Exam(name!!, description!!, Date(System.currentTimeMillis()), questionList)
    }
}
