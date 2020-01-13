package ru.bmstu.testsystem.sessions.config

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.servlet.config.annotation.EnableWebMvc
import ru.bmstu.testsystem.sessions.service.SimpleUserDetailService


@Order(1)
@Configuration
@EnableWebSecurity
@EnableWebMvc
@ComponentScan
class SecurityConfiguration : WebSecurityConfigurerAdapter() {

    @Autowired
    private val userDetailService: SimpleUserDetailService? = null

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        http
            .csrf().disable()
            .authorizeRequests()
            .antMatchers("/login**", "/oauth/authorize", "/bootstrap/**", "/css/**", "/js/**", "/img/**").permitAll()
            .antMatchers("/tokens/**").permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin().permitAll() //.loginPage("/login").permitAll()
            .and().logout().permitAll().clearAuthentication(true).invalidateHttpSession(true)
    }

    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder) {
        auth
            .userDetailsService(userDetailService)
    }

    @Bean
    fun passwordEncoder(): BCryptPasswordEncoder {
        return BCryptPasswordEncoder()
    }
}