package ru.bmstu.testsystem.exams.service

import org.springframework.data.domain.Page
import ru.bmstu.testsystem.exams.model.ExamDataIn
import ru.bmstu.testsystem.exams.model.ExamDataOut
import ru.bmstu.testsystem.exams.model.ExamDataWithAnsOut

import java.util.UUID

interface ExamService {
    fun findById(id: UUID, flag: Int): ExamDataOut

    fun addExam(exam: ExamDataIn): ExamDataOut

    fun removeExam(id: UUID)

    fun incPasses(id: UUID): ExamDataOut

    fun decPasses(id: UUID): ExamDataOut

    fun getAllExams(page: Int, limit: Int): Page<ExamDataOut>

    fun findByIdAdmin(id: UUID): ExamDataWithAnsOut

}
