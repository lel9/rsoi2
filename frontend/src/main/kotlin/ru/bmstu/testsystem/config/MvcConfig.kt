package ru.bmstu.testsystem.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate
import org.springframework.web.servlet.config.annotation.InterceptorRegistry
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import ru.bmstu.testsystem.controller.handler.InactivityFilter
import ru.bmstu.testsystem.controller.handler.OAuth2TokenFilter
import ru.bmstu.testsystem.services.TokenServiceImpl


@Configuration
class MvcConfig : WebMvcConfigurer {

    @Value("\${spring.security.oauth2.provider.authorization-uri}")
    lateinit var authorization: String

    @Value("\${spring.security.oauth2.provider.check-token-uri}")
    lateinit var check: String

    @Value("\${spring.security.oauth2.client.client-id}")
    lateinit var client: String

    @Value("\${spring.security.oauth2.client.client-secret}")
    lateinit var secret: String

    @Value("\${spring.security.oauth2.client.scope}")
    lateinit var scope: String

    @Value("\${spring.security.oauth2.client.redirect-uri-template}")
    lateinit var redirect: String

    @Value("\${spring.security.oauth2.provider.revoke-access-token-uri}")
    lateinit var revokeAccess: String

    @Value("\${spring.security.oauth2.provider.revoke-refresh-token-uri}")
    lateinit var revokeRefresh: String

    @Autowired
    private lateinit var tokenServiceImpl: TokenServiceImpl

    override fun addInterceptors(registry: InterceptorRegistry) {
        registry.addInterceptor(InactivityFilter(redirect, client, secret, revokeAccess, revokeRefresh))
        registry.addInterceptor(OAuth2TokenFilter(authorization, check, client, secret, scope, redirect, tokenServiceImpl))
    }


    override fun addResourceHandlers(registry: ResourceHandlerRegistry) {
        registry.addResourceHandler("/bootstrap/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/bootstrap/4.0.0/")
    }

    @Bean
    fun restTemplate(): RestTemplate? {
        val restTemplate = RestTemplate()
        return restTemplate
    }


}
