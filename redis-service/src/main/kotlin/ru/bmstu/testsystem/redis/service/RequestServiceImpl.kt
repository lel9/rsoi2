package ru.bmstu.testsystem.redis.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service
import ru.bmstu.testsystem.redis.model.RequestIn
import ru.bmstu.testsystem.redis.model.RequestOut
import ru.bmstu.testsystem.redis.repository.RequestRepository
import java.util.*

@Service("requestService")
class RequestServiceImpl : RequestService {

    @Autowired
    private lateinit var repo: RequestRepository

    override fun save(request: RequestIn) {
        repo.save(ru.bmstu.testsystem.redis.domian.Request(request))
    }

    override fun deleteRequest(id: String) {
        repo.deleteById(id)
    }

    override fun getRequests(page: Int, limit: Int, host: String, port: Int): Page<RequestOut> {
        val pageableRequest = PageRequest.of(page, limit)
        val requests = repo.findAllByHostAndPort(host, port, pageableRequest)
        return requests.map { request -> RequestOut(request) }
    }
}