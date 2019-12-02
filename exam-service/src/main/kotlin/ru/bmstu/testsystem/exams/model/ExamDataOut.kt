package ru.bmstu.testsystem.exams.model

import ru.bmstu.testsystem.exams.domain.Exam
import java.util.*

data class ExamDataOut (
    var id: String,
    var name: String,
    var passes: Int,
    var description: String,
    var questionIns: List<QuestionDataOut>
)
{
    constructor(exam: Exam): this(exam.id.toString(), exam.name, exam.passCount, exam.description,
        exam.questions.mapIndexed { i, q -> QuestionDataOut(q) })

}
