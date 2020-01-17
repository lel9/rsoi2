package ru.bmstu.testsystem.gateway.web

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import io.swagger.annotations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.beans.propertyeditors.CustomDateEditor
import org.springframework.data.domain.Page
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.ResourceAccessException
import ru.bmstu.testsystem.gateway.model.*
import ru.bmstu.testsystem.gateway.web.util.ProxyService
import ru.bmstu.testsystem.gateway.web.util.RestPageImpl
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import java.util.logging.Logger
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull


@RestController
@RequestMapping("/api/v1/gateway")
@Api(value = "Testing system")
class CompositRestApiProxy {

    var log: Logger = Logger.getLogger(CompositRestApiProxy::class.java.getName())

    @Autowired
    private lateinit var proxyService: ProxyService

    @Value("\${services.exam.host}")
    private lateinit var exam_host: String

    @Value("\${services.exam.port}")
    private var exam_port: Int? = null

    @Value("\${services.user.host}")
    private lateinit var user_host: String

    @Value("\${services.user.port}")
    private var user_port: Int? = null

    @Value("\${services.result.host}")
    private lateinit var result_host: String

    @Value("\${services.result.port}")
    private var result_port: Int? = null

    @Value("\${services.redis.host}")
    private lateinit var redis_host: String

    @Value("\${services.redis.port}")
    private var redis_port: Int? = null

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
        val entityRes = proxyService.proxy(null, HttpMethod.GET, request, result_host, result_port!!, "/api/v1/result/get")

        val results = jacksonObjectMapper().readValue<Page<Result>>(entityRes.body!!, object : TypeReference<RestPageImpl<Result>>() {})
        val map: MutableMap<String, String> = mutableMapOf(Pair("ignore_deleted_flag", "1"))

        val list = results.map { result ->
            var user: UserData? = null
            var exam: ExamDataOut? = null
            var entityUser: ResponseEntity<String>? = null
            var entityExam: ResponseEntity<String>? = null

            try {
                entityUser = proxyService.proxy(null, HttpMethod.GET, request, user_host, user_port!!, "/api/v1/user/get/${result.userId}")
            } catch (ex: ResourceAccessException) {
            }
            if (entityUser != null && entityUser.statusCode == HttpStatus.OK) {
                user = jacksonObjectMapper().readValue<UserData>(entityUser.body!!)
            }

            try {
                entityExam = proxyService.proxy(null, HttpMethod.GET, request, exam_host, exam_port!!, "/api/v1/exam/get/${result.examId}", map)
            } catch (ex: ResourceAccessException) {
            }
            if (entityExam != null && entityExam.statusCode == HttpStatus.OK) {
                exam = jacksonObjectMapper().readValue<ExamDataOut>(entityExam.body!!)
            }
            ResultComposit(result, exam, user)
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
    fun deleteUser(@PathVariable id: String, request: HttpServletRequest): ResponseEntity<String> {
        UUID.fromString(id)
        try {
            proxyService.proxy(null, HttpMethod.DELETE, request, user_host, user_port!!,  "/api/v1/user/delete/$id")
        } catch (ex: ResourceAccessException) {
            val req = RequestIn("DELETE", user_host, user_port!!,  "/api/v1/user/delete/$id")
            proxyService.proxy(jacksonObjectMapper().writeValueAsString(req), HttpMethod.POST, redis_host, redis_port!!, "/api/v1/redis/request/add")
        }
        try {
            proxyService.proxy(null, HttpMethod.DELETE, request, result_host, result_port!!,  "/api/v1/result/delete/$id")
        } catch (ex: ResourceAccessException) {
            val req = RequestIn("DELETE", result_host, result_port!!, "/api/v1/result/delete/$id")
            proxyService.proxy(jacksonObjectMapper().writeValueAsString(req), HttpMethod.POST, redis_host, redis_port!!, "/api/v1/redis/request/add")
        }
        return ResponseEntity.noContent().build()
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
                  @RequestBody @Valid
                  @NotNull(message="Список ответов должен быть задан")
                  @NotEmpty(message="Список ответов не может быть пуст") body: List<UserAnswer>, request: HttpServletRequest
    ): ResponseEntity<String> {
        UUID.fromString(examId)
        UUID.fromString(userId)

        val res = proxyService.proxy(null, HttpMethod.POST, request, exam_host, exam_port!!, "/api/v1/exam/inc/$examId")
        if (res.statusCode != HttpStatus.OK)
            return res

        val examEntity = proxyService.proxy(null, HttpMethod.GET, request, exam_host, exam_port!!, "/api/v1/exam/get/full/$examId")
        if (examEntity.statusCode != HttpStatus.OK) {
            rollbackIncPasses(request, examId)
            return examEntity
        }

        val exam = jacksonObjectMapper().readValue<ExamDataWithAnsOut>(examEntity.body!!)
        val ua = UserAnswers(exam.questionIns, body)
        var resadd: ResponseEntity<String>? = null

        try {
            resadd = proxyService.proxy(jacksonObjectMapper().writeValueAsString(ua), HttpMethod.POST, request, result_host, result_port!!, "/api/v1/result/add")
        } catch (ex: ResourceAccessException) {
            rollbackIncPasses(request, examId)
            throw ex
        }

        if (resadd.statusCode != HttpStatus.CREATED)
            rollbackIncPasses(request, examId)
        return resadd

    }

    fun rollbackIncPasses(request: HttpServletRequest, examId: String) {
        try {
            val res = proxyService.proxy(null, HttpMethod.POST, request, exam_host, exam_port!!, "/api/v1/exam/dec/$examId")
            log.info("Откат операции...")
            if (res.statusCode != HttpStatus.OK)
                log.warning("Откат операции неуспешен: " + res.body)
        } catch (ex: ResourceAccessException) {
            log.warning("Откат операции неуспешен: " + ex.message)
        }
    }
}
