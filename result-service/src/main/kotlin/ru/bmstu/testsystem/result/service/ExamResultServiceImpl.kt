package ru.bmstu.testsystem.result.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service
import ru.bmstu.testsystem.result.domain.ExamResult
import ru.bmstu.testsystem.result.domain.QuestionType
import ru.bmstu.testsystem.result.model.UserAnswers
import ru.bmstu.testsystem.result.repository.ExamResultRepository
import ru.bmstu.testsystem.result.model.Result
import java.sql.Date
import java.util.*

@Service("examResultService")
class ExamResultServiceImpl : ExamResultService {

    @Autowired
    private lateinit var examResultRepository: ExamResultRepository

    override fun deleteAll(userId: UUID) {
        examResultRepository.deleteByUserId(userId)
    }

    override fun getAllResults(page: Int, limit: Int): List<Result> {
        val pageableRequest = PageRequest.of(page, limit, Sort.by(Sort.Direction.ASC, "passedAt"))
        val exams = examResultRepository.findAll(pageableRequest)

        val list = ArrayList<Result>()
        exams.forEach { res ->
            list.add(
                Result(res)
            )
        }
        return list
    }

    override fun passTest(examId: UUID, userId: UUID, userAnswers: UserAnswers): Result {
        if (userAnswers.list == null || userAnswers.list!!.isEmpty())
            throw IllegalArgumentException("Список ответов не задан")
        if (userAnswers.questions == null || userAnswers.questions!!.isEmpty())
            throw IllegalArgumentException("Список вопросов не задан")

        val resultString = userAnswers.list?.map { mua ->
            mua?.let { ua ->
                val mq = userAnswers.questions?.find { q -> q?.id == ua.questionId  }
                mq?.let { q ->
                    if (q.type == QuestionType.NO_ANSWER) {
                        if (ua.checkedVariants != null)
                            throw IllegalArgumentException("Не заданы варианты ответа")
                        return@map q.correctInputAnswer?.toLowerCase() == ua.inputAnswer?.toLowerCase()
                    }
                    if (ua.inputAnswer != null)
                        throw IllegalArgumentException("Не задан ответ")
                    return@map q.correctVariants?.toSet() == ua.checkedVariants?.toSet()
                }
            }
        } ?. filter { it == true } ?. count().toString() + "/" + userAnswers.questions?.count().toString()

        val result = ExamResult(resultString, Date(System.currentTimeMillis()), examId, userId)
        val saved = examResultRepository.save(result)
        return Result(saved)
    }
}
