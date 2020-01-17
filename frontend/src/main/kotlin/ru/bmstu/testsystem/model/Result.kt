package ru.bmstu.testsystem.model

import java.sql.Date
import java.util.*

data class Result (
    val id: UUID,

    val result: String,

    val passedAt: Date,

    var examId: UUID,

    var userId: UUID
)
