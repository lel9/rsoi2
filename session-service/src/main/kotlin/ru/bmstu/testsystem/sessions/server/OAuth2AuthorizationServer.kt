package ru.bmstu.testsystem.sessions.server

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer
import org.springframework.security.oauth2.provider.token.DefaultTokenServices
import org.springframework.security.oauth2.provider.token.TokenStore
import ru.bmstu.testsystem.sessions.service.SimpleUserDetailService


@Configuration
@EnableAuthorizationServer
class OAuth2AuthorizationServer : AuthorizationServerConfigurerAdapter() {
    @Autowired
    private val passwordEncoder: BCryptPasswordEncoder? = null

    @Autowired
    private val tokenStore: TokenStore? = null

    @Autowired
    private val userDetailService: SimpleUserDetailService? = null

    @Throws(Exception::class)
    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer) {
        endpoints
            .tokenStore(tokenStore)
            .reuseRefreshTokens(false)
            .userDetailsService(userDetailService)
    }

    @Throws(Exception::class)
    override fun configure(security: AuthorizationServerSecurityConfigurer) {
        security
            .tokenKeyAccess("permitAll()")
            .checkTokenAccess("isAuthenticated()")
    }

    @Throws(Exception::class)
    override fun configure(clients: ClientDetailsServiceConfigurer) {
        clients
            .inMemory()
            .withClient("SampleClientId").secret(passwordEncoder!!.encode("secret"))
            .authorizedGrantTypes("password", "authorization_code", "refresh_token")
            .authorities("READ_ONLY_CLIENT")
            .scopes("user_info")
            .autoApprove(true)
            .redirectUris("http://localhost:3000/login/oauth2/code/")
            .accessTokenValiditySeconds(30)
            .refreshTokenValiditySeconds(604800)
    }

    @Bean
    @Primary
    fun tokenServices(): DefaultTokenServices? {
        val tokenServices = DefaultTokenServices()
        tokenServices.setSupportRefreshToken(true)
        tokenServices.setReuseRefreshToken(false)
        tokenServices.setTokenStore(tokenStore)
        return tokenServices
    }
}