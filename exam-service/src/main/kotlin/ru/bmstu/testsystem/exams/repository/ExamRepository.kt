package ru.bmstu.testsystem.exams.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import ru.bmstu.testsystem.exams.domain.Exam
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
@Transactional
interface ExamRepository : CrudRepository<Exam, UUID> {

    fun findByIsDeleted(status: Boolean): List<Exam>

    fun findByIsDeleted(status: Boolean, pageable: Pageable): Page<Exam>

    @Modifying
    @Query("update exams set is_deleted = TRUE where id = ?1",
            nativeQuery = true)
    fun setDeletedById(id: UUID)

    fun findAll(pageable: Pageable): Page<Exam>
}
