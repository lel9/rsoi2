package ru.bmstu.testsystem.redis.web

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import ru.bmstu.testsystem.redis.model.RequestIn
import ru.bmstu.testsystem.redis.model.RequestOut
import ru.bmstu.testsystem.redis.service.RequestService
import java.util.*

@RestController
@RequestMapping("/api/v1/redis")
class RestApiImpl {

    @Autowired
    private lateinit var requestService: RequestService

    @PostMapping("/request/add")
    @ResponseStatus(HttpStatus.CREATED)
    fun addRequest(@RequestBody request: RequestIn) {
        requestService.save(request)
    }

    @GetMapping("/request/get")
    @ResponseStatus(HttpStatus.OK)
    fun getAllUsers(@RequestParam(value = "host", required = true) host: String,
                    @RequestParam(value = "port", required = true) port: Int,
                    @RequestParam(value = "page", defaultValue = "0", required = false) page: Int,
                    @RequestParam(value = "limit", defaultValue = "12", required = false) limit: Int): Page<RequestOut> {
        return requestService.getRequests(page, limit, host, port)
    }

    @DeleteMapping("/request/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteRequest(@PathVariable id: String) {
        requestService.deleteRequest(id)
    }
}