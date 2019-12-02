package ru.bmstu.testsystem.result.model

import ru.bmstu.testsystem.result.domain.ExamResult
import java.sql.Date
import java.util.*

data class Result (
    val id: UUID,

    val result: String,

    val passedAt: Date,

    var examId: UUID,

    var userId: UUID
) {
    constructor(res: ExamResult): this(res.id, res.result, res.passedAt, res.examId, res.userId)
}
