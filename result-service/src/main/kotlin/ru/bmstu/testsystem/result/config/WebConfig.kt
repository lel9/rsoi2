package ru.bmstu.testsystem.result.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import ru.bmstu.testsystem.result.web.handler.TokenValidationHandler
import javax.crypto.Cipher

@Configuration
class WebConfig: WebMvcConfigurer {

    //@Value("\${server.address}")
    private var host: String = "result-service"

    @Value("\${server.port}")
    private var port: Int? = null

    @Autowired
    private lateinit var cipher: Cipher

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(TokenValidationHandler(host, port!!, cipher))
    }

    @Bean
    fun getCipher(): Cipher {
        return Cipher.getInstance("AES")
    }
}