package ru.bmstu.testsystem.gateway.web.util

import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.client.RestTemplate
import java.net.URI
import javax.servlet.http.HttpServletRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.http.*
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.util.UriComponentsBuilder
import ru.bmstu.testsystem.gateway.model.AppCredential
import ru.bmstu.testsystem.gateway.model.resolve
import ru.bmstu.testsystem.gateway.web.CompositRestApiProxy
import java.util.logging.Logger


@Service
class ProxyService {
    @Autowired
    private lateinit var restTemplate: RestTemplate

    private var map: MutableMap<String, String?> = mutableMapOf()

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

        return execute(host, port, body, thirdPartyApi, method, headers)
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

        return execute(host, port, body, URI.create(builder.toUriString()), method, headers)
    }

    fun proxy(
        body: String?,
        method: HttpMethod,
        host: String,
        port: Int,
        path: String
    ): ResponseEntity<String> {
        val thirdPartyApi = URI("http", null, host, port, path, "", null)

        val headers = HttpHeaders()
        headers.add("Content-Type", "application/json")

        return execute(host, port, body, thirdPartyApi, method, headers)
    }

    private fun execute(host: String, port: Int, body: String?, thirdPartyApi: URI, method: HttpMethod, headers: HttpHeaders): ResponseEntity<String> {
        val credential = resolve(host, port)!!
        var token = map[credential.appId]
        headers.add(HttpHeaders.AUTHORIZATION, token)
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
            val ecode = e.statusCode
            val ebody = e.responseBodyAsString
            if (ecode == HttpStatus.UNAUTHORIZED) {
                token = restTemplate.postForObject("http://$host:$port/api/v1/token", credential, String::class.java)
                if (map.containsKey(credential.appId))
                    map.replace(credential.appId, token)
                else
                    map.put(credential.appId, token)
                headers.replace(HttpHeaders.AUTHORIZATION, mutableListOf(token))
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
            }
            //val headers= e.responseHeaders
            log.info(ebody)
            log.info(ecode.toString())
            e.printStackTrace()
            return ResponseEntity(ebody, ecode)
        }
    }

    @Bean
    fun restTemplateBuild(builder: RestTemplateBuilder): RestTemplate {
        return builder.build()
    }
}

