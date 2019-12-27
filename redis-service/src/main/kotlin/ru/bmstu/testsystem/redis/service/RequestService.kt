package ru.bmstu.testsystem.redis.service

import org.springframework.data.domain.Page
import ru.bmstu.testsystem.redis.model.RequestIn
import ru.bmstu.testsystem.redis.model.RequestOut

interface RequestService {
    fun save(request: RequestIn)
    fun getRequests(page: Int, limit: Int, host: String, port: Int): Page<RequestOut>
    fun deleteRequest(id: String)
}