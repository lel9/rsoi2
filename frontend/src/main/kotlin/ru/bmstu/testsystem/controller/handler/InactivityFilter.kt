package ru.bmstu.testsystem.controller.handler

import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.core.annotation.Order
import org.springframework.http.RequestEntity
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.RestTemplate
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import org.springframework.web.util.WebUtils
import java.net.URI
import java.time.Clock
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Order(1)
class InactivityFilter(var redirect: String, var client: String, var secret: String,
                       var revokeAccess: String, var revokeRefresh: String): HandlerInterceptorAdapter() {

    private val clock = Clock.systemUTC()

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (request.requestURL.toString() == redirect ||
            request.requestURI == "/all_exams" ||
            request.requestURI == "/")
            return true

        if (clock.instant().toEpochMilli() > request.session.lastAccessedTime + 30000) {
            invalidateTokens(request)
        }

        return true
    }

    private fun invalidateTokens(httpRequest: HttpServletRequest) {
        try {
            val accessTokenCookie = WebUtils.getCookie(httpRequest, "access_token")
            if (accessTokenCookie != null) {
                val requestEntity = RequestEntity
                        .post(URI.create("$revokeAccess/${accessTokenCookie.value}"))
                        .build()
                restTemplate(client, secret).exchange(requestEntity, String::class.java)
            }
        } catch (e: HttpStatusCodeException) {}

        try {
            val refreshTokenCookie = WebUtils.getCookie(httpRequest, "refresh_token")
            if (refreshTokenCookie != null) {
                val requestEntity = RequestEntity
                        .post(URI.create("$revokeRefresh/${refreshTokenCookie.value}"))
                        .build()
                restTemplate(client, secret).exchange(requestEntity, String::class.java)
            }
        } catch (e: HttpStatusCodeException) {}
    }

    private fun restTemplate(clientId: String, clientSecret: String): RestTemplate {
        return RestTemplateBuilder()
                .basicAuthentication(clientId, clientSecret)
                .build()
    }


}