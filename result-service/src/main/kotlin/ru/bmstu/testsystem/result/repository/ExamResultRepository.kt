package ru.bmstu.testsystem.result.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import ru.bmstu.testsystem.result.domain.ExamResult
import java.util.*

@Repository
@Transactional
interface ExamResultRepository : CrudRepository<ExamResult, UUID> {

    fun findAll(pageable: Pageable): Page<ExamResult>

    fun deleteByUserId(id: UUID)
}
