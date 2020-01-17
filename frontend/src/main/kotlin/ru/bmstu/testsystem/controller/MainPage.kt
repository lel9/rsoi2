package ru.bmstu.testsystem.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import ru.bmstu.testsystem.services.ExamServiceImpl
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
class MainPage {

    @Autowired
    private lateinit var examService: ExamServiceImpl

    @GetMapping("/main_page")
    fun getMainPage(request: HttpServletRequest,
                    response: HttpServletResponse,
                    model: Model): String {
        model.addAttribute("title", "Список тестов")
        model.addAttribute("exams", examService.getExams(0, 12, request, response))
        return "main_page"
    }
}
