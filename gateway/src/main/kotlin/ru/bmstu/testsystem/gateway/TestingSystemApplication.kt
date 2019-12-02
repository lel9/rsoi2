package ru.bmstu.testsystem.gateway

import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration
import org.springframework.boot.runApplication
import org.springframework.context.annotation.Bean
import org.springframework.web.client.RestTemplate
import org.springframework.boot.web.client.RestTemplateBuilder


@SpringBootApplication
@EnableAutoConfiguration(exclude=[DataSourceAutoConfiguration::class, HibernateJpaAutoConfiguration::class])
class TestingSystemApplication

fun main(args: Array<String>) {
    runApplication<TestingSystemApplication>(*args)
}


