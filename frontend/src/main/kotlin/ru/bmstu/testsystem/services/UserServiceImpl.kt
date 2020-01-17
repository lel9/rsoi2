package ru.bmstu.testsystem.services

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.Page
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.RestTemplate
import ru.bmstu.testsystem.model.ExamDataOut
import ru.bmstu.testsystem.model.RegistrationData
import ru.bmstu.testsystem.model.UserData
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Service("userService")
class UserServiceImpl : UserService {

    @Autowired
    private lateinit var proxyService: ProxyService

    @Value("\${gateway.host}")
    private lateinit var host: String

    @Value("\${gateway.port}")
    private var port: Int? = null

    override fun getUsers(page: Int, limit: Int, request: HttpServletRequest, response: HttpServletResponse): Page<UserData> {
        val map: MutableMap<String, String> = mutableMapOf(Pair("page", page.toString()), Pair("limit", limit.toString()))

        val forObject = proxyService.proxy(null, HttpMethod.GET, request, response,
                host, port!!, "/api/v1/gateway/user/get", map)

        return jacksonObjectMapper().readValue(forObject.body!!, object : TypeReference<RestPageImpl<UserData>>() {
        })
    }

    override fun findById(id: String, request: HttpServletRequest, response: HttpServletResponse): UserData {
        val res = proxyService.proxy(null, HttpMethod.GET, request, response, host, port!!, "/api/v1/gateway/user/get/${id}", null)
        return jacksonObjectMapper().readValue(res.body, UserData::class.java)
    }


    override fun updateUser(newUserData: UserData, request: HttpServletRequest, response: HttpServletResponse): UserData? {
        try {
            val res = proxyService.proxy(newUserData, HttpMethod.POST, request, response, host, port!!,"/api/v1/gateway/user/edit", null)
            return jacksonObjectMapper().readValue(res.body, UserData::class.java)
        } catch (e: HttpStatusCodeException) {
            val code = e.statusCode
            if (code == HttpStatus.CONFLICT)
                return null
            throw e
        }
    }

    override fun deleteUser(id: String, request: HttpServletRequest, response: HttpServletResponse) {
        proxyService.proxy(null, HttpMethod.DELETE, request, response, host, port!!, "/api/v1/gateway/user/delete/${id}", null)
    }

    override fun registerUser(rd: RegistrationData, request: HttpServletRequest, response: HttpServletResponse): UserData? {
        try {
            val res = proxyService.proxy(rd, HttpMethod.POST, request, response, host, port!!, "/api/v1/gateway/user/register", null)
            return jacksonObjectMapper().readValue(res.body, UserData::class.java)
        } catch (e: HttpStatusCodeException) {
            val code = e.statusCode
            if (code == HttpStatus.CONFLICT)
                return null
            throw e
        }
    }
}
