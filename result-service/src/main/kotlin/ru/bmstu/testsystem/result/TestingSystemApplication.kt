package ru.bmstu.testsystem.result

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.boot.runApplication
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.event.EventListener
import org.springframework.data.domain.Page
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.web.client.RestTemplate
import ru.bmstu.testsystem.result.model.RequestOut
import ru.bmstu.testsystem.result.web.util.RestPageImpl
import java.net.URI
import java.util.logging.Logger
import javax.annotation.PostConstruct

@SpringBootApplication
class TestingSystemApplication {

    //@Value("\${server.address}")
    private val host: String = "result-service"

    @Value("\${server.port}")
    private val port: Int? = null

    @Value("\${redis.host}")
    private lateinit var redis_host: String

    @Value("\${redis.port}")
    private var redis_port: Int? = null

    @Autowired
    private lateinit var restTemplate: RestTemplate


    var log: Logger = Logger.getLogger(TestingSystemApplication::class.java.getName())

    @EventListener(ApplicationReadyEvent::class)
    fun readFromRedis() {
        var page: Page<RequestOut>? = getRequests(0) ?: return

        processPage(page!!)

        while (!page!!.isLast) {
            page = getRequests(page.number+1) ?: return
            processPage(page)
        }
    }

    fun getRequests(page: Int) : Page<RequestOut>? {
        val thirdPartyApi =
            URI("http", null, redis_host, redis_port!!, "/api/v1/redis/request/get", "host=$host&port=$port&page=$page", null)

        val headers = HttpHeaders()
        headers.add("Accept", "application/json")

        try {
            val res = restTemplate.exchange(thirdPartyApi, HttpMethod.GET, HttpEntity<String>(headers), String::class.java)
            return jacksonObjectMapper().readValue<Page<RequestOut>>(res.body!!, object : TypeReference<RestPageImpl<RequestOut>>() {})
        } catch (e: Exception) {
            log.warning(e.message)
        }
        return null
    }

    fun processPage(requests: Page<RequestOut>) {
        requests.forEach { request ->
            if (request.method.equals("DELETE")) {
                try {
                    val thirdPartyApiRequest = URI("http", null, host, port!!, request.path, "", null)
                    restTemplate.exchange(thirdPartyApiRequest, HttpMethod.DELETE, null, String::class.java)

                    val thirdPartyApiDelete = URI("http", null, redis_host, redis_port!!, "/api/v1/redis/request/delete/${request.id}", "", null)
                    restTemplate.exchange(thirdPartyApiDelete, HttpMethod.DELETE, null, String::class.java)

                } catch (ex: Exception) {
                    log.warning(ex.message)
                }
            }

        }
    }

    @Bean
    fun restTemplateBuild(builder: RestTemplateBuilder): RestTemplate {
        return builder.build()
    }
}

fun main(args: Array<String>) {
    runApplication<TestingSystemApplication>(*args)
}
