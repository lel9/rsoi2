package ru.bmstu.testsystem.controller

import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping

@Controller
class Index {

    @GetMapping("/")
    fun index(): String {
        return "redirect:/all_exams"
    }
}
