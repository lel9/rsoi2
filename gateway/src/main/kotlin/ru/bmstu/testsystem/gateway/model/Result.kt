package ru.bmstu.testsystem.gateway.model

import java.sql.Date
import java.util.*

data class Result (
    val id: String,

    val result: String,

    val passedAt: Date,

    var examId: String,

    var userId: String
)
