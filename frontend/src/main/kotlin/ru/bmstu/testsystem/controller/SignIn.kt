package ru.bmstu.testsystem.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import ru.bmstu.testsystem.services.TokenServiceImpl
import java.time.Clock
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Controller
class SignIn {

    @Autowired
    private lateinit var tokenService: TokenServiceImpl

    private val clock = Clock.systemUTC()

    @GetMapping("/login/oauth2/code")
    fun writeCookieExample(model: Model, request: HttpServletRequest, response: HttpServletResponse): String? {
        val code = request.getParameter("code")
        if (code == null) {
            model.addAttribute("info", "Не удалось авторизовать пользователя")
            return "error_page"
        }
        tokenService.getAccessToken(code, response)
        if (request.getParameter("state") == null)
            return "redirect:/all_exams"

        return "redirect:${request.getParameter("state")}"
    }
}