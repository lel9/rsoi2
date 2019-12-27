package ru.bmstu.testsystem.result.web

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.propertyeditors.CustomDateEditor
import org.springframework.http.HttpStatus
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.*
import org.springframework.data.domain.Page
import ru.bmstu.testsystem.result.model.Result
import ru.bmstu.testsystem.result.model.UserAnswers
import ru.bmstu.testsystem.result.service.ExamResultServiceImpl
import java.text.DateFormat
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/result")
class RestApiImpl {

    @Autowired
    lateinit var resultService: ExamResultServiceImpl

    @InitBinder
    fun initBinder(binder: WebDataBinder) {
        binder.registerCustomEditor(
                Date::class.java, CustomDateEditor(DateFormat.getInstance(), true)
        )
    }

    @GetMapping("/get")
    @ResponseStatus(HttpStatus.OK)
    fun getAll(@RequestParam(value = "page", defaultValue = "0", required = false) page: Int,
               @RequestParam(value = "limit", defaultValue = "12", required = false) limit: Int) : Page<Result> {
        return resultService.getAllResults(page, limit)
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    fun postRes(@RequestParam(value = "examId", required = true) examId: String,
                     @RequestParam(value = "userId", required = true) userId: String,
                     @RequestBody @Valid userAnswers: UserAnswers): Result {
        val testResult = resultService.passTest(
            examId = UUID.fromString(examId),
            userId = UUID.fromString(userId),
            userAnswers = userAnswers)
        return testResult
    }

    @DeleteMapping("/delete/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteRes(@PathVariable userId: String) {
        resultService.deleteAll(UUID.fromString(userId))
    }
}
