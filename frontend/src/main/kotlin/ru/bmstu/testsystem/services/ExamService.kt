package ru.bmstu.testsystem.services

import org.springframework.data.domain.Page
import ru.bmstu.testsystem.model.ExamDataIn
import ru.bmstu.testsystem.model.ExamDataOut

import java.util.UUID
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

interface ExamService {
    fun addExam(exam: ExamDataIn, request: HttpServletRequest, response: HttpServletResponse): ExamDataOut
    fun getExams(page: Int, limit: Int, request: HttpServletRequest, response: HttpServletResponse): Page<ExamDataOut>
    fun findById(id: String, request: HttpServletRequest, response: HttpServletResponse): ExamDataOut
    fun deleteExam(id: String, request: HttpServletRequest, response: HttpServletResponse)
}
