package ru.bmstu.testsystem.exams.web

import ru.bmstu.testsystem.exams.model.ExamDataIn
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.propertyeditors.CustomDateEditor
import org.springframework.http.HttpStatus
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.*
import java.text.DateFormat
import java.util.*
import org.springframework.web.bind.annotation.RestController
import ru.bmstu.testsystem.exams.model.ExamDataOut
import ru.bmstu.testsystem.exams.model.ExamDataWithAnsOut
import ru.bmstu.testsystem.exams.service.ExamServiceImpl


@RestController
@RequestMapping("/api/v1/exam")
class RestApiImpl {

    @Autowired
    private lateinit var examService: ExamServiceImpl


    @InitBinder
    fun initBinder(binder: WebDataBinder) {
        binder.registerCustomEditor(
                Date::class.java, CustomDateEditor(DateFormat.getInstance(), true)
        )
    }

    @GetMapping("/get/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getExam(@RequestParam(name="ignore_deleted_flag", defaultValue = "0", required = false) flag: Int,
                @PathVariable id: String): ExamDataOut {
        return examService.findById(UUID.fromString(id), flag)
    }

    @GetMapping("/get/full/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getAdminExam(@PathVariable id: String): ExamDataWithAnsOut {
        return examService.findByIdAdmin(UUID.fromString(id))
    }

    @GetMapping("/get")
    @ResponseStatus(HttpStatus.OK)
    fun getAll(@RequestParam(value = "page", defaultValue = "0") page: Int,
               @RequestParam(value = "limit", defaultValue = "12") limit: Int) : List<ExamDataOut> {
        return examService.getAllExams(page, limit)
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    fun addExam(@RequestBody  exam: ExamDataIn): ExamDataOut {
        val addExam = examService.addExam(exam)
        return addExam
    }

    @DeleteMapping("/remove/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun removeExam(@PathVariable  id: String) {
        examService.removeExam(UUID.fromString(id))
    }

    @PostMapping("/inc/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun incPasses(@PathVariable id: String) : ExamDataOut{
        return examService.incPasses(UUID.fromString(id))
    }

}
