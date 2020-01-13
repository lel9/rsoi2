package ru.bmstu.testsystem.gateway.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import ru.bmstu.testsystem.gateway.web.filter.OAuth2TokenFilter

@Configuration
class WebConfig: WebMvcConfigurer {
    @Value("\${spring.security.oauth2.provider.check-token-uri}")
    lateinit var check: String

    @Value("\${spring.security.oauth2.client.client-id}")
    lateinit var client: String

    @Value("\${spring.security.oauth2.client.client-secret}")
    lateinit var secret: String

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(OAuth2TokenFilter(check, client, secret))
    }
}