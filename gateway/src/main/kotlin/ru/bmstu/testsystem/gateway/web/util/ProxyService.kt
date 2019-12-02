package ru.bmstu.testsystem.gateway.web.util

import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.client.RestTemplate
import java.net.URI
import javax.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.web.util.UriComponentsBuilder




@Service
class ProxyService {
    @Autowired
    private lateinit var restTemplate: RestTemplate

    fun proxy(
        @RequestBody body: String?, method: HttpMethod,
        request: HttpServletRequest,
        host: String,
        port: Int,
        path: String
    ): ResponseEntity<String> {
        val thirdPartyApi =
            URI("http", null, host, port, path, request.getQueryString(), null)

        val headerNames = request.headerNames
        val headers = HttpHeaders()
        while (headerNames.hasMoreElements()) {
            val nextElement = headerNames.nextElement()
            val header = request.getHeader(nextElement)
            headers.add(nextElement, header)
        }
        return if (body != null) {
            restTemplate.exchange(thirdPartyApi, method, HttpEntity<String>(body, headers), String::class.java)
        } else {
            restTemplate.exchange(thirdPartyApi, method, HttpEntity<String>(headers), String::class.java)
        }
    }

    fun proxy(
        @RequestBody body: String?, method: HttpMethod,
        request: HttpServletRequest,
        host: String,
        port: Int,
        path: String,
        params: Map<String, String>
    ): ResponseEntity<String> {
        val thirdPartyApi =
            URI("http", null, host, port, path, request.getQueryString(), null)

        val builder = UriComponentsBuilder.fromHttpUrl(thirdPartyApi.toURL().toString())
        params.forEach { t, u -> builder.queryParam(t, u) }

        val headerNames = request.headerNames
        val headers = HttpHeaders()
        while (headerNames.hasMoreElements()) {
            val nextElement = headerNames.nextElement()
            val header = request.getHeader(nextElement)
            headers.add(nextElement, header)
        }
        return if (body != null) {
            restTemplate.exchange(builder.toUriString(), method, HttpEntity<String>(body, headers), String::class.java)
        } else {
            restTemplate.exchange(builder.toUriString(), method, HttpEntity<String>(headers), String::class.java)
        }
    }


    @Bean
    fun restTemplateBuild(builder: RestTemplateBuilder): RestTemplate {
        return builder.build()
    }
}

