package ru.bmstu.testsystem.gateway.web.filter

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.RequestEntity
import org.springframework.stereotype.Component
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.RestTemplate
import org.springframework.web.servlet.HandlerInterceptor
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import org.springframework.web.util.WebUtils
import ru.bmstu.testsystem.gateway.model.GenericResponse
import java.net.URI
import javax.security.auth.login.CredentialNotFoundException
import javax.servlet.Filter
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class OAuth2TokenFilter(var check: String, var client: String, var secret: String): HandlerInterceptorAdapter() {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (request.requestURI == "/api/v1/gateway/exam/get" ||
            request.requestURI.startsWith("/swagger-ui.html") ||
            request.requestURI.startsWith("/webjars") ||
            request.requestURI == "/swagger-resources")
            return true

        val httpRequest = request as HttpServletRequest
        var tokenCookie = WebUtils.getCookie(httpRequest, "token")
        if (tokenCookie == null) {
            val bodyOfResponse = GenericResponse("", "CredentialNotFound")
            val res = response as HttpServletResponse

            res.status = HttpStatus.UNAUTHORIZED.value()
            res.contentType = MediaType.APPLICATION_JSON_UTF8_VALUE

            res.outputStream.write(ObjectMapper().writeValueAsString(bodyOfResponse).toByteArray())

            return false
        } else {
            val formParameters = LinkedMultiValueMap<String, String>()
            formParameters.add("token", tokenCookie.value)

            val requestEntity = RequestEntity
                .post(URI.create(check))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(formParameters)

            try {
                restTemplate(client, secret).exchange(requestEntity, String::class.java)
            } catch (e: HttpStatusCodeException) {
                if (e.responseBodyAsString.contains("invalid_token")) {
                    val bodyOfResponse = GenericResponse("", "CredentialNotFound")
                    val res = response as HttpServletResponse

                    res.status = HttpStatus.UNAUTHORIZED.value()
                    res.contentType = MediaType.APPLICATION_JSON_UTF8_VALUE

                    res.outputStream.write(ObjectMapper().writeValueAsString(bodyOfResponse).toByteArray())

                    return false
                }
                val bodyOfResponse = GenericResponse("", "CredentialExpired")
                val res = response as HttpServletResponse

                res.status = HttpStatus.UNAUTHORIZED.value()
                res.contentType = MediaType.APPLICATION_JSON_UTF8_VALUE

                res.outputStream.write(ObjectMapper().writeValueAsString(bodyOfResponse).toByteArray())

                return false
            }
        }
        return true
    }

    private fun restTemplate(clientId: String, clientSecret: String): RestTemplate {
        return RestTemplateBuilder()
            .basicAuthentication(clientId, clientSecret)
            .build()
    }


}