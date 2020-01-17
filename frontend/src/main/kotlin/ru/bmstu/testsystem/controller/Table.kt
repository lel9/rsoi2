package ru.bmstu.testsystem.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.propertyeditors.CustomDateEditor
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.*
import ru.bmstu.testsystem.services.ExamServiceImpl
import ru.bmstu.testsystem.services.TableServiceImpl
import ru.bmstu.testsystem.services.UserServiceImpl
import java.text.DateFormat
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
class Table {

    @Autowired
    private lateinit var tableService: TableServiceImpl

    @Autowired
    private lateinit var userService: UserServiceImpl

    @Autowired
    private lateinit var examService: ExamServiceImpl

    @InitBinder
    fun initBinder(binder: WebDataBinder) {
        binder.registerCustomEditor(
                java.util.Date::class.java, CustomDateEditor(DateFormat.getInstance(), true)
        )
    }

    @GetMapping("/all_users")
    fun getAllUsers(@RequestParam(value = "page", defaultValue = "0", required = false) page: Int,
                    @RequestParam(value = "limit", defaultValue = "12", required = false) limit: Int,
                    request: HttpServletRequest, response: HttpServletResponse,
                    model: Model): String {
        model.addAttribute("title", "Все пользователи")
        model.addAttribute("table", tableService.getUserTable(page, limit, request, response))
        return "table"
    }

    @GetMapping("/all_exams")
    fun getAllExams(@RequestParam(value = "page", defaultValue = "0", required = false) page: Int,
                    @RequestParam(value = "limit", defaultValue = "12", required = false) limit: Int,
                    request: HttpServletRequest, response: HttpServletResponse,
                    model: Model): String {
        model.addAttribute("title", "Все тесты")
        model.addAttribute("table", tableService.getExamTable(page, limit, request, response))
        return "table"
    }

    @GetMapping("/all_results")
    fun getStatistic(@RequestParam(value = "page", defaultValue = "0", required = false) page: Int,
                     @RequestParam(value = "limit", defaultValue = "12", required = false) limit: Int,
                     request: HttpServletRequest, response: HttpServletResponse,
                     model: Model): String {
        model.addAttribute("title", "Результаты")
        model.addAttribute("table", tableService.getResultsTable(page, limit, request, response))
        return "table"
    }

    @GetMapping("/delete/user/{id}")
    fun deleteUser(@PathVariable id: String,
                   request: HttpServletRequest, response: HttpServletResponse,
                   model: Model): String {
        userService.deleteUser(id, request, response)
        model.addAttribute("title", "Все пользователи")
        model.addAttribute("table", tableService.getUserTable(0, 12, request, response))
        return "redirect:/all_users"
    }

    @GetMapping("/delete/exam/{id}")
    fun deleteExam(@PathVariable id: String,
                   request: HttpServletRequest, response: HttpServletResponse,
                   model: Model): String {
        examService.deleteExam(id, request, response)
        model.addAttribute("title", "Все тесты")
        model.addAttribute("table", tableService.getExamTable(0, 12, request, response))
        return "redirect:/all_exams"
    }
}
