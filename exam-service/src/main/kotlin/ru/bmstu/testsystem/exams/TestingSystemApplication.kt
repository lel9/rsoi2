package ru.bmstu.testsystem.exams

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication



@SpringBootApplication
class TestingSystemApplication

fun main(args: Array<String>) {
    runApplication<TestingSystemApplication>(*args)
}
