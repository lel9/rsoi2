package ru.bmstu.testsystem.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.propertyeditors.CustomDateEditor
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.*
import ru.bmstu.testsystem.model.UserAnswer
import ru.bmstu.testsystem.model.UserAnswers
import ru.bmstu.testsystem.services.ExamResultServiceImpl
import ru.bmstu.testsystem.services.ExamServiceImpl
import ru.bmstu.testsystem.services.UserServiceImpl
import java.text.DateFormat
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
class ExamPage {

    @Autowired
    lateinit var examService: ExamServiceImpl

    @Autowired
    lateinit var resultService: ExamResultServiceImpl

    @Autowired
    lateinit var userService: UserServiceImpl


    @InitBinder
    fun initBinder(binder: WebDataBinder) {
        binder.registerCustomEditor(
                Date::class.java, CustomDateEditor(DateFormat.getInstance(), true)
        )
    }

    @GetMapping("exam_page/{id}")
    fun getTestPage(@PathVariable id: String, model: Model,
                    request: HttpServletRequest, response: HttpServletResponse): String {
        val exam = examService.findById(id, request, response)
        val ua = UserAnswers(arrayOfNulls<UserAnswer>(exam.questionIns.size).toMutableList())

        model.addAttribute("userAnswers",  ua)
        model.addAttribute("users", userService.getUsers(0, 12, request, response))
        model.addAttribute("userId", "")
        model.addAttribute("exam", exam)

        return "exam_page"
    }

    @PostMapping("exam_page/{id}")
    fun postTestPage(@PathVariable id: String,
                     @ModelAttribute userAnswers: UserAnswers,
                     request: HttpServletRequest, response: HttpServletResponse,
                     model: Model): String {
        val testResult = resultService.passTest(id, userAnswers, request, response)
        model.addAttribute("res", testResult)
        return "result"

    }
}
