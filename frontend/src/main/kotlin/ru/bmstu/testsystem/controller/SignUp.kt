package ru.bmstu.testsystem.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import ru.bmstu.testsystem.model.RegistrationData
import ru.bmstu.testsystem.services.UserServiceImpl
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Controller
class SignUp {

    @Autowired
    private lateinit var userService: UserServiceImpl

    @GetMapping("/sign_up")
    fun registrationForm(model: Model): String {
        model.addAttribute("registration_data", RegistrationData())
        return "sign_up"
    }

    @PostMapping("/sign_up")
    fun registrationSubmit(@ModelAttribute rd: RegistrationData, request: HttpServletRequest, response: HttpServletResponse): String {
        val user = userService.registerUser(rd, request, response) ?: return "redirect:/sign_up?exist=true"
        return "redirect:/edit_profile/${user.id}"
    }

}
