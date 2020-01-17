package ru.bmstu.testsystem.services

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import ru.bmstu.testsystem.model.Result
import ru.bmstu.testsystem.model.ResultComposit
import ru.bmstu.testsystem.model.UserAnswers
import ru.bmstu.testsystem.model.UserData
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Service("examResultService")
class ExamResultServiceImpl : ExamResultService {

    @Autowired
    private lateinit var proxyService: ProxyService

    @Value("\${gateway.host}")
    private lateinit var host: String

    @Value("\${gateway.port}")
    private var port: Int? = null

    override fun passTest(exam: String, userAnswers: UserAnswers, request: HttpServletRequest, response: HttpServletResponse): Result {
        val map: MutableMap<String, String?> = mutableMapOf(Pair("examId", exam), Pair("userId", userAnswers.userId))

        val forObject = proxyService.proxy(userAnswers.list, HttpMethod.POST, request, response,
                host, port!!, "/api/v1/gateway/result/add", map)

        return jacksonObjectMapper().readValue(forObject.body!!, Result::class.java)

    }

    override fun getResults(page: Int, limit: Int, request: HttpServletRequest, response: HttpServletResponse): Page<ResultComposit> {
        val map: MutableMap<String, String> = mutableMapOf(Pair("page", page.toString()), Pair("limit", limit.toString()))

        val forObject = proxyService.proxy(null, HttpMethod.GET, request, response,
                host, port!!, "/api/v1/gateway/results", map)

        return jacksonObjectMapper().readValue(forObject.body!!, object : TypeReference<RestPageImpl<ResultComposit>>() {
        })
    }
}
