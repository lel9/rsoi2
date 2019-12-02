package ru.bmstu.testsystem.exams.service

import ru.bmstu.testsystem.exams.exception.DeletedExamException
import ru.bmstu.testsystem.exams.exception.NoExamException
import ru.bmstu.testsystem.exams.repository.ExamRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import ru.bmstu.testsystem.exams.model.ExamDataIn
import ru.bmstu.testsystem.exams.model.ExamDataOut
import ru.bmstu.testsystem.exams.model.ExamDataWithAnsOut
import java.util.*
import kotlin.IllegalArgumentException

@Service("examService")
class ExamServiceImpl : ExamService {

    @Autowired
    private lateinit var examRepository: ExamRepository

    override fun findById(id: UUID, flag: Int): ExamDataOut {
        val exam = examRepository.findById(id)
        if (!exam.isPresent) throw NoExamException()
        val examGet = exam.get()
        if (examGet.isDeleted && flag == 1) throw DeletedExamException()
        return ExamDataOut(examGet)
    }

    override fun findByIdAdmin(id: UUID): ExamDataWithAnsOut {
        val exam = examRepository.findById(id)
        if (!exam.isPresent) throw NoExamException()
        val examGet = exam.get()
        if (examGet.isDeleted) throw DeletedExamException()
        return ExamDataWithAnsOut(examGet)
    }

    override fun addExam(exam: ExamDataIn) : ExamDataOut {
        if (exam.questionIns.isEmpty())
            throw IllegalArgumentException("Не задан список вопросов")
        return ExamDataOut(examRepository.save(exam.toExam()))
    }

    override fun getAllExams(page: Int, limit: Int): List<ExamDataOut> {
        val pageableRequest = PageRequest.of(page, limit, Sort.by(Sort.Direction.ASC, "name"))
        val exams = examRepository.findAll(pageableRequest)

        val list = ArrayList<ExamDataOut>()
        exams.forEach { user ->
            list.add(
                ExamDataOut(user)
            )
        }
        return list
    }

    override fun removeExam(id: UUID) {
        if (!examRepository.findById(id).isPresent)
            throw NoExamException()
        if (examRepository.findById(id).get().isDeleted)
            throw DeletedExamException()
        examRepository.setDeletedById(id)
    }

    override fun incPasses(id: UUID): ExamDataOut {
        val byId = examRepository.findById(id)
        if (!byId.isPresent) throw NoExamException()
        var exam = byId.get()
        if (exam.isDeleted) throw DeletedExamException()
        exam.passCount += 1
        val save = examRepository.save(exam)
        return ExamDataOut(save)
    }
}
