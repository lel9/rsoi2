package ru.bmstu.testsystem.services

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import org.springframework.web.util.WebUtils
import ru.bmstu.testsystem.model.ErrorData
import java.net.URI
import java.time.Clock
import java.util.logging.Logger
import javax.security.auth.login.CredentialNotFoundException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Service
class ProxyService {
    @Autowired
    private lateinit var restTemplate: RestTemplate

    @Autowired
    private lateinit var tokenService: TokenServiceImpl

    var log: Logger = Logger.getLogger(ProxyService::class.java.getName())

    private val clock = Clock.systemUTC()

    fun proxy(
            body: Any?, method: HttpMethod,
            request: HttpServletRequest,
            response: HttpServletResponse,
            host: String,
            port: Int,
            path: String,
            params: Map<String, String?>?
    ): ResponseEntity<String> {
        val thirdPartyApi =
                URI("http", null, host, port, path, "", null)

        val builder = UriComponentsBuilder.fromHttpUrl(thirdPartyApi.toURL().toString())
        params?.forEach { t, u -> builder.queryParam(t, u) }

//        val headerNames = request.headerNames
        val headers = HttpHeaders()
//        while (headerNames.hasMoreElements()) {
//            val nextElement = headerNames.nextElement()
//            val header = request.getHeader(nextElement)
//            headers.add(nextElement, header)
//        }

        headers.add(HttpHeaders.CONTENT_TYPE, "application/json")

        var accessToken = request.getAttribute("access_token") //refreshToken(request, response, false)
        if (accessToken != null)
            headers.add("Cookie", "token=${accessToken}")

        try {
            return executeRequest(body, builder, method, headers)
        } catch (e: HttpStatusCodeException) {
            val error = jacksonObjectMapper().readValue(e.responseBodyAsString, ErrorData::class.java)
            if (error.type.equals("CredentialExpired")) {
                val newAccessToken = refreshToken(request, response)
                headers.replace("Cookie", mutableListOf("token=${newAccessToken}"))
                return executeRequest(body, builder, method, headers)
            }
            throw e
        }
    }

    private fun executeRequest(body: Any?, builder: UriComponentsBuilder, method: HttpMethod, headers: HttpHeaders): ResponseEntity<String> {
        return if (body != null) {
            val asString = jacksonObjectMapper().writeValueAsString(body)
            restTemplate.exchange(builder.toUriString(), method, HttpEntity<String>(asString, headers), String::class.java)
        } else {
            restTemplate.exchange(builder.toUriString(), method, HttpEntity<String>(headers), String::class.java)
        }
    }

    private fun refreshToken(request: HttpServletRequest, response: HttpServletResponse): String? {
        //val accessTokenCookie = request.getAttribute( "access_token")
        val refreshTokenCookie = request.getAttribute("refresh_token")
        //val expiredAt = request.cookies?.find { c -> c.name.equals("expired_at") }
        //if (expiredAt == null || isExpired(expiredAt = expiredAt.value.toLong()) || always) {
        if (refreshTokenCookie != null) {
            val newAccessToken = tokenService.refreshToken(refreshTokenCookie.toString(), response)
            if (newAccessToken != null) {
                return newAccessToken.access_token
            } else {
                throw CredentialNotFoundException()
            }
        } else {
            throw CredentialNotFoundException()
        }
    }

//    private fun isExpired(expiredAt: Long?): Boolean {
//        val now = this.clock.instant().toEpochMilli()
//        expiredAt ?: return true
//        return now > (expiredAt + 10000)
//    }

    private fun restTemplate(clientId: String, clientSecret: String): RestTemplate {
        return RestTemplateBuilder()
                .basicAuthentication(clientId, clientSecret)
                .build()
    }
}