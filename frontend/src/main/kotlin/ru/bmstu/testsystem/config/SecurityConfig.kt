package ru.bmstu.testsystem.config

import org.springframework.context.annotation.Configuration
//import org.springframework.security.config.annotation.web.builders.HttpSecurity
//import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
//
//@Configuration
//class SecurityConfig : WebSecurityConfigurerAdapter() {
//    override fun configure(http: HttpSecurity) {
//        http.antMatcher("/**")
//                .authorizeRequests()
//                .antMatchers("/", "/login**", "/all_exams", "/all_users", "/all_results",
//                        "/bootstrap/**", "/css/**", "/js/**", "/img/**")
//                .permitAll()
//                .anyRequest()
//                .authenticated()
//                .and()
//                .oauth2Login()
//    }
//}