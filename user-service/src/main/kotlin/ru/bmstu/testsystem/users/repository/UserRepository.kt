package ru.bmstu.testsystem.users.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import ru.bmstu.testsystem.users.domain.User
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Repository
@Transactional
interface UserRepository : CrudRepository<User, UUID> {
    fun findByUsername(name: String): User?
    fun findAll(pageable: Pageable): Page<User>
}
