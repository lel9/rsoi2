package ru.bmstu.testsystem.sessions.web

import org.springframework.web.bind.annotation.*
import java.security.Principal


@RestController
class UserController {
    @GetMapping("/user/me")
    fun user(principal: Principal): String {
        return principal.name
    }
}