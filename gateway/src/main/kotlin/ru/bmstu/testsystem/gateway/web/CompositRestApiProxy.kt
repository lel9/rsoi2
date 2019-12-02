package ru.bmstu.testsystem.gateway.web

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.swagger.annotations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpMethod
import org.springframework.beans.propertyeditors.CustomDateEditor
import org.springframework.http.HttpStatus
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.*
import org.springframework.http.ResponseEntity
import ru.bmstu.testsystem.gateway.model.*
import ru.bmstu.testsystem.gateway.web.util.ProxyService
import ru.bmstu.testsystem.gateway.model.Result
import java.util.*
import javax.servlet.http.HttpServletRequest
import java.text.DateFormat
import java.text.SimpleDateFormat

@RestController
@RequestMapping("/api/v1/gateway")
@Api(value = "Testing system")
class CompositRestApiProxy {

    @Autowired
    private lateinit var proxyService: ProxyService

    @InitBinder
    fun initBinder(binder: WebDataBinder) {
        binder.registerCustomEditor(
                Date::class.java, CustomDateEditor(DateFormat.getInstance(), true)
        )
    }

    @GetMapping("/results")
    @ApiOperation(value = "Get all results", response = List::class)
    @ApiResponses(
        value = [
            ApiResponse(code = 200, message = "Successfully retrieved list")
        ]
    )
    fun getResults(
        @ApiParam(value = "page", required = false, defaultValue = "0")
        @RequestParam(value = "page", required = false, defaultValue = "0") page: Int,
        @ApiParam(value = "limit", required = false, defaultValue = "12")
        @RequestParam(value = "limit", required = false, defaultValue = "12") limit: Int,
        request: HttpServletRequest
    ): ResponseEntity<String> {
        val entityRes = proxyService.proxy(null, HttpMethod.GET, request, "localhost", 8083, "/api/v1/result/get")
        val results = jacksonObjectMapper().readValue<List<Result>>(entityRes.body!!)
        val map: MutableMap<String, String> = mutableMapOf()
        map.put("ignore_deleted_flag", "1")
        val list = arrayListOf<ResultComposit>()
        results.forEach { result ->
            val entityUser = proxyService.proxy(null, HttpMethod.GET, request, "localhost", 8081, "/api/v1/user/get/${result.userId}")
            if (entityUser.statusCode == HttpStatus.OK) {
                val user = jacksonObjectMapper().readValue<UserData>(entityUser.body!!)
                val entityExam =
                    proxyService.proxy(null, HttpMethod.GET, request, "localhost", 8082, "/api/v1/exam/get/${result.examId}", map)
                if (entityExam.statusCode == HttpStatus.OK) {
                    val exam = jacksonObjectMapper().readValue<ExamDataOut>(entityExam.body!!)
                    list.add(ResultComposit(result, exam, user))
                }
            }
        }

        val objectMapper = ObjectMapper();
        //Set pretty printing of json
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT)
        objectMapper.dateFormat = SimpleDateFormat("yyyy-MM-dd")

        return ResponseEntity.ok().body(objectMapper.writeValueAsString(list))
    }

    @DeleteMapping("/user/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ApiOperation(value = "Delete user")
    @ApiResponses(value = [
        ApiResponse(code = 204, message = "Successfully deleted user and his results"),
        ApiResponse(code = 404, message = "User not found", response = ErrorData::class),
        ApiResponse(code = 400, message = "Bad request", response = ErrorData::class)
    ])
    fun deleteUser(@PathVariable id: String, request: HttpServletRequest) {
        proxyService.proxy(null, HttpMethod.DELETE, request,"localhost", 8081,  "/api/v1/user/delete/$id")
        proxyService.proxy(null, HttpMethod.DELETE, request,"localhost", 8083,  "/api/v1/result/delete/$id")
    }

    @PostMapping("/result/add")
    @ApiOperation(value = "Pass exam and add result")
    @ApiResponses(value = [
        ApiResponse(code = 201, message = "Successfully created result", response = Result::class),
        ApiResponse(code = 404, message = "User or exam not found", response = ErrorData::class),
        ApiResponse(code = 400, message = "Bad request", response = ErrorData::class)
    ])
    fun addResult(@ApiParam(value = "Exam id", required = true)
                  @RequestParam examId: String,
                  @ApiParam(value = "User id", required = true)
                  @RequestParam userId: String,
                  @ApiParam(value = "Answers", required = true)
                  @RequestBody body: List<UserAnswer>, request: HttpServletRequest
    ): ResponseEntity<String> {
        proxyService.proxy(null, HttpMethod.POST, request, "localhost", 8082, "/api/v1/exam/inc/$examId")

        val examEntity = proxyService.proxy(null, HttpMethod.GET, request, "localhost", 8082, "/api/v1/exam/get/full/$examId")
        if (examEntity.statusCode != HttpStatus.OK)
            return examEntity
        val exam = jacksonObjectMapper().readValue<ExamDataWithAnsOut>(examEntity.body!!)
        val ua = UserAnswers(exam.questionIns, body)
        return proxyService.proxy(jacksonObjectMapper().writeValueAsString(ua), HttpMethod.POST, request, "localhost", 8083, "/api/v1/result/add")
    }
}
