package ru.bmstu.testsystem.services

import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.http.*
//import org.springframework.security.oauth2.core.AuthorizationGrantType
//import org.springframework.security.oauth2.core.OAuth2AccessToken
//import org.springframework.security.oauth2.core.OAuth2AuthenticationException
//import org.springframework.security.oauth2.core.OAuth2AuthorizationException
//import org.springframework.security.oauth2.core.endpoint.OAuth2AccessTokenResponse
//import org.springframework.security.oauth2.core.endpoint.OAuth2ParameterNames
import org.springframework.stereotype.Service
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.RestTemplate
import ru.bmstu.testsystem.model.Oauth2Token
import ru.bmstu.testsystem.model.Oauth2TokenSimple
//import ru.bmstu.testsystem.handler.BearerTokenInterceptor
import java.net.URI
import java.time.Clock
import javax.servlet.http.Cookie
import javax.servlet.http.HttpServletResponse


@Service
class TokenServiceImpl: TokenService {

    @Value("\${spring.security.oauth2.provider.token-uri}")
    lateinit var token: String

    @Value("\${spring.security.oauth2.client.redirect-uri-template}")
    lateinit var redirect: String

    @Value("\${spring.security.oauth2.client.client-id}")
    lateinit var client: String

    @Value("\${spring.security.oauth2.client.client-secret}")
    lateinit var secret: String

    @Value("\${spring.security.oauth2.provider.user-info-uri}")
    lateinit var userinfo: String

    private val clock = Clock.systemUTC()

    override fun getUsername(): String? {
        return restTemplate(client, secret).getForObject(userinfo, String::class.java)
    }

    override fun getAccessToken(code: String, response: HttpServletResponse) {
        val formParameters = LinkedMultiValueMap<String, String>()
        //payload=grant_type=authorization_code&code=zU7OD7&redirect_uri=http%3A%2F%2Flocalhost%3A3000%2Flogin%2Foauth2%2Fcode%2F
        formParameters.add("grant_type", "authorization_code")
        formParameters.add("code", code)
        formParameters.add("redirect_uri", redirect)

        val requestEntity = RequestEntity
                .post(URI.create(token))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(formParameters)

        val responseEntity = restTemplate(client, secret).exchange(requestEntity, Oauth2Token::class.java).body!!

        saveCookie(response, responseEntity)
    }

    override fun refreshToken(refreshToken: String?, response: HttpServletResponse): Oauth2TokenSimple? {
        if (refreshToken == null) {
            //BearerTokenInterceptor.log.info("Failed to refresh token")
            return null
        }

        val formParameters = LinkedMultiValueMap<String, String>()
        formParameters.add("grant_type", "refresh_token")
        formParameters.add("refresh_token", refreshToken)
        formParameters.add("redirect_uri", redirect)

        val requestEntity = RequestEntity
                .post(URI.create(token))
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE)
                .body(formParameters)

        val r = restTemplate(client, secret)
        val responseEntity: Oauth2Token
        try {
            responseEntity = r.exchange(requestEntity, Oauth2Token::class.java).body!!
        } catch (e: HttpStatusCodeException) {
            return null
        }

        saveCookie(response, responseEntity)
        return Oauth2TokenSimple(responseEntity.access_token, responseEntity.refresh_token)
    }

    private fun saveCookie(response: HttpServletResponse, responseEntity: Oauth2Token) {
        val cookie = Cookie("access_token", responseEntity.access_token)
        //cookie.setMaxAge(responseEntity.expires_in.toInt())
        cookie.isHttpOnly = true
        cookie.path = "/"
        response.addCookie(cookie)

        val cookie2 = Cookie("refresh_token", responseEntity.refresh_token)
        cookie2.isHttpOnly = true
        cookie2.path = "/"
        response.addCookie(cookie2)

//        val toEpochMilli = clock.instant().plusSeconds(responseEntity.expires_in).toEpochMilli()
//        val cookie3 = Cookie("expired_at", toEpochMilli.toString())
//        cookie3.setMaxAge(responseEntity.expires_in.toInt())
//        cookie3.isHttpOnly = true
//        cookie3.path = "/"
//        response.addCookie(cookie3)
    }

    private fun restTemplate(clientId: String, clientSecret: String): RestTemplate {
        return RestTemplateBuilder()
                .basicAuthentication(clientId, clientSecret)
                .build()
    }
}