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
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.util.UriComponentsBuilder
import ru.bmstu.testsystem.gateway.web.CompositRestApiProxy
import java.util.logging.Logger


@Service
class ProxyService {
    @Autowired
    private lateinit var restTemplate: RestTemplate

    var log: Logger = Logger.getLogger(ProxyService::class.java.getName())

    fun proxy(
        body: String?,
        method: HttpMethod,
        request: HttpServletRequest,
        host: String,
        port: Int,
        path: String
    ): ResponseEntity<String> {
        log.info("request to $host:$port/$path")

        val thirdPartyApi =
            URI("http", null, host, port, path, request.getQueryString(), null)

        val headerNames = request.headerNames
        val headers = HttpHeaders()
        while (headerNames.hasMoreElements()) {
            val nextElement = headerNames.nextElement()
            val header = request.getHeader(nextElement)
            headers.add(nextElement, header)
        }
        try {
            if (body != null) {
                return restTemplate.exchange(
                            thirdPartyApi,
                            method,
                            HttpEntity(body, headers),
                            String::class.java
                        )
            } else {
                return restTemplate.exchange(thirdPartyApi, method, HttpEntity<String>(headers), String::class.java)
            }
        } catch (e: HttpStatusCodeException) {
            val code = e.statusCode
            val body = e.responseBodyAsString
            //val headers= e.responseHeaders
            return ResponseEntity(body, code)
        }
    }

    fun proxy(
        body: String?, method: HttpMethod,
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

    fun proxy(
        body: String?,
        method: HttpMethod,
        host: String,
        port: Int,
        path: String
    ): ResponseEntity<String> {
        val thirdPartyApi = URI("http", null, host, port, path, "", null)

        val builder = UriComponentsBuilder.fromHttpUrl(thirdPartyApi.toURL().toString())
        val headers = HttpHeaders()
        headers.add("Content-Type", "application/json")

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

