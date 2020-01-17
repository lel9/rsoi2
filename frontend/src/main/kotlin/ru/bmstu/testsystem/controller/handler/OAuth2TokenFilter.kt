package ru.bmstu.testsystem.controller.handler

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.core.annotation.Order
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.RequestEntity
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.RestTemplate
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import org.springframework.web.util.WebUtils
import ru.bmstu.testsystem.services.TokenServiceImpl
import java.net.URI
import java.time.Clock
import javax.security.auth.login.CredentialNotFoundException
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Order(2)
class OAuth2TokenFilter(var authorization: String,
                        var check: String,
                        var client: String,
                        var secret: String,
                        var scope: String,
                        var redirect: String,
                        var tokenServiceImpl: TokenServiceImpl) : HandlerInterceptorAdapter() {


    private val clock = Clock.systemUTC()

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (request.requestURL.toString() == redirect ||
            request.requestURI == "/all_exams" ||
            request.requestURI == "/")
            return true

        val accessTokenCookie = WebUtils.getCookie(request, "access_token")
        val refreshTokenCookie = WebUtils.getCookie(request, "refresh_token")


        if (accessTokenCookie == null) {
            redirectToAuthorization(response)
//            if (refreshTokenCookie == null)
//                redirectToAuthorization(response)
//            else
//                refreshToken(refreshTokenCookie.value, request, response)

        } else {
            val formParameters = LinkedMultiValueMap<String, String>()
            formParameters.add("token", accessTokenCookie.value)

            val requestEntity = RequestEntity
                    .post(URI.create(check))
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                    .body(formParameters)

            try {
                restTemplate(client, secret).exchange(requestEntity, String::class.java)
                request.setAttribute("access_token", accessTokenCookie.value)
                request.setAttribute("refresh_token", refreshTokenCookie?.value)
            } catch (e: HttpStatusCodeException) {
                if (e.responseBodyAsString.contains("Token has expired")) {
                    if (refreshTokenCookie != null) {
                        refreshToken(refreshTokenCookie.value, request, response)
                    } else {
                        redirectToAuthorization(response)
                    }
                } else {
                    redirectToAuthorization(response)
                }
            }
        }
        return true
    }

    private fun refreshToken(refreshToken: String?, request: HttpServletRequest, response: HttpServletResponse) {
        val newAccessToken = tokenServiceImpl.refreshToken(refreshToken, response)
        if (newAccessToken == null) {
            redirectToAuthorization(response)
        } else {
            request.setAttribute("access_token", newAccessToken.access_token)
            request.setAttribute("refresh_token", newAccessToken.refresh_token)
        }
    }

    private fun redirectToAuthorization(response: HttpServletResponse) {
        throw CredentialNotFoundException()
    }

    private fun restTemplate(clientId: String, clientSecret: String): RestTemplate {
        return RestTemplateBuilder()
                .basicAuthentication(clientId, clientSecret)
                .build()
    }
}