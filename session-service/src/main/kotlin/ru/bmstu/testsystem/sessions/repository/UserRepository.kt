package ru.bmstu.testsystem.sessions.repository

import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import ru.bmstu.testsystem.sessions.domain.User
import java.util.*

@Repository
@Transactional
interface UserRepository : CrudRepository<User, UUID> {
    fun findByUsername(name: String): User?
}
