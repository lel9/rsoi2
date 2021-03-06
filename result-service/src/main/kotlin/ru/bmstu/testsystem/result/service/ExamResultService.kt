package ru.bmstu.testsystem.result.service

import org.springframework.data.domain.Page
import ru.bmstu.testsystem.result.model.UserAnswers
import ru.bmstu.testsystem.result.model.Result
import java.util.*

interface ExamResultService {
    fun passTest(examId: UUID, userId: UUID, userAnswers: UserAnswers): Result
    fun deleteAll(userId: UUID)
    fun getAllResults(page: Int, limit: Int): Page<Result>
}
