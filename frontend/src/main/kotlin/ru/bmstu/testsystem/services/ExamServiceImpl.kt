package ru.bmstu.testsystem.services

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import org.springframework.web.client.RestTemplate
import ru.bmstu.testsystem.model.ExamDataIn
import ru.bmstu.testsystem.model.ExamDataOut
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Service("examService")
class ExamServiceImpl : ExamService {

    @Autowired
    private lateinit var proxyService: ProxyService


    @Value("\${gateway.host}")
    private lateinit var host: String

    @Value("\${gateway.port}")
    private var port: Int? = null

    override fun findById(id: String, request: HttpServletRequest, response: HttpServletResponse): ExamDataOut {
        val res = proxyService.proxy(null, HttpMethod.GET, request, response, host, port!!, "/api/v1/gateway/exam/get/${id}", null)
        return jacksonObjectMapper().readValue(res.body, ExamDataOut::class.java)
    }

    override fun getExams(page: Int, limit: Int, request: HttpServletRequest, response: HttpServletResponse): Page<ExamDataOut> {
        val map: MutableMap<String, String> = mutableMapOf(Pair("page", page.toString()), Pair("limit", limit.toString()))

        val forObject = proxyService.proxy(null, HttpMethod.GET,request, response,
                host, port!!,"/api/v1/gateway/exam/get", map)

        return jacksonObjectMapper().readValue(forObject.body!!, object : TypeReference<RestPageImpl<ExamDataOut>>() {
        })
    }

    override fun addExam(exam: ExamDataIn, request: HttpServletRequest, response: HttpServletResponse) : ExamDataOut {
        val res = proxyService.proxy(exam, HttpMethod.POST, request, response, host, port!!, "/api/v1/gateway/exam/add", null)
        return jacksonObjectMapper().readValue(res.body, ExamDataOut::class.java)
    }

    override fun deleteExam(id: String, request: HttpServletRequest, response: HttpServletResponse) {
        proxyService.proxy(null, HttpMethod.DELETE, request, response, host, port!!, "/api/v1/gateway/exam/delete/${id}", null)
    }
}
