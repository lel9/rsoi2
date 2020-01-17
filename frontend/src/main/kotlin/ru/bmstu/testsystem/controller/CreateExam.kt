package ru.bmstu.testsystem.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.propertyeditors.CustomDateEditor
import org.springframework.http.HttpMethod
//import org.springframework.security.core.context.SecurityContextHolder
//import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.*
import java.text.DateFormat
import java.util.*
import ru.bmstu.testsystem.model.ExamDataIn
import ru.bmstu.testsystem.services.ExamServiceImpl
import ru.bmstu.testsystem.services.ProxyService
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


@Controller
class CreateTest {

    @Autowired
    private lateinit var examService: ExamServiceImpl


    @InitBinder
    fun initBinder(binder: WebDataBinder) {
        binder.registerCustomEditor(
                Date::class.java, CustomDateEditor(DateFormat.getInstance(), true)
        )
    }

    @GetMapping("/create_exam")
    fun getCreateTest(model: Model): String {
        model.addAttribute("exam", ExamDataIn())
        return "create_exam"
    }

    @PostMapping("/create_exam")
    fun postCreateTest(model: Model, @ModelAttribute("exam")  exam: ExamDataIn,
                       request: HttpServletRequest, response: HttpServletResponse): String {
        val addExam = examService.addExam(exam, request, response)
        model.addAttribute("exam", addExam)
        return "redirect:/exam_page/${addExam.id}"
    }

}
