package ru.bmstu.testsystem.gateway.model

import java.sql.Date
import java.util.*

data class ResultComposit (
    val id: UUID,

    val result: String,

    val passedAt: Date,

    var exam: ExamDataOut,

    var user: UserData
) {
    constructor(res: Result, exam: ExamDataOut, user: UserData) : this(res.id, res.result, res.passedAt, exam, user)
}
