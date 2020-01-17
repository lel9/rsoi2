package ru.bmstu.testsystem.services

import org.springframework.data.domain.Page
import ru.bmstu.testsystem.model.Result
import ru.bmstu.testsystem.model.ResultComposit
import ru.bmstu.testsystem.model.UserAnswers
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

interface ExamResultService {
    fun passTest(exam: String, userAnswers: UserAnswers, request: HttpServletRequest, response: HttpServletResponse): Result
    fun getResults(page: Int, limit: Int, request: HttpServletRequest, response: HttpServletResponse): Page<ResultComposit>
}
