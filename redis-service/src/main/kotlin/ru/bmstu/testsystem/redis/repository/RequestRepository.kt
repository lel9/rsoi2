package ru.bmstu.testsystem.redis.repository

import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import ru.bmstu.testsystem.redis.domian.Request

@Repository
interface RequestRepository : CrudRepository<Request?, String?> {
    fun findAllByHostAndPort(host: String, port: Int, pageable: Pageable): Page<Request>
    fun findAllByHostAndPort(host: String, port: Int): List<Request>
}