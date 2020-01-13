package ru.bmstu.testsystem.sessions

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer

@SpringBootApplication
@EnableResourceServer
class AuthorizationServerApplication {
}

fun main(args: Array<String>) {
    var pe = BCryptPasswordEncoder()
    var s = pe.encode("test")
    runApplication<AuthorizationServerApplication>(*args)
}