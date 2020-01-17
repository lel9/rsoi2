package ru.bmstu.testsystem.gateway.web

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.swagger.annotations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.client.ResourceAccessException
import ru.bmstu.testsystem.gateway.model.ErrorData
import ru.bmstu.testsystem.gateway.model.ExamDataIn
import ru.bmstu.testsystem.gateway.model.ExamDataOut
import ru.bmstu.testsystem.gateway.web.util.ProxyService
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid


@RestController
@RequestMapping("/api/v1/gateway")
@Api(value = "Testing system")
class ExamsRestApiProxy {

    @Autowired
    private lateinit var proxyService: ProxyService

    @Value("\${services.exam.host}")
    private lateinit var exam_host: String

    @Value("\${services.exam.port}")
    private var exam_port: Int? = null

    @GetMapping("/exam/get")
    @ApiOperation(value = "Get all exams", response = List::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved list")
    ])
    fun getExams(@ApiParam(value = "page", required = false, defaultValue = "0")
                 @RequestParam(value = "page", required = false, defaultValue = "0") page: Int,
                 @ApiParam(value = "limit", required = false, defaultValue = "12")
                 @RequestParam(value = "limit", required = false, defaultValue = "12") limit: Int,
                 request: HttpServletRequest
    ): ResponseEntity<String> {
        return proxyService.proxy(null, HttpMethod.GET, request, exam_host, exam_port!!, "/api/v1/exam/get")
    }

    @GetMapping("/exam/get/{id}")
    @ApiOperation(value = "Get exam by id to pass", response = ExamDataOut::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved exam"),
        ApiResponse(code = 404, message = "Exam not found", response = ErrorData::class),
        ApiResponse(code = 400, message = "Bad request", response = ErrorData::class)
    ])
    fun getExamById(@PathVariable id: String, request: HttpServletRequest): ResponseEntity<String> {
        UUID.fromString(id)
        return proxyService.proxy(null, HttpMethod.GET, request, exam_host, exam_port!!, "/api/v1/exam/get/$id")
    }

    @PostMapping("/exam/add")
    @ApiOperation(value = "Add exam")
    @ApiResponses(value = [
        ApiResponse(code = 201, message = "Successfully created exam", response = ExamDataOut::class),
        ApiResponse(code = 400, message = "Bad request", response = ErrorData::class)
    ])
    fun addExam(@ApiParam(value = "New exam object", required = true)
                     @RequestBody @Valid body: ExamDataIn, request: HttpServletRequest
    ): ResponseEntity<String> {
        val string = jacksonObjectMapper().writeValueAsString(body)
        return proxyService.proxy(string, HttpMethod.POST, request, exam_host, exam_port!!, "/api/v1/exam/add")
    }

    @DeleteMapping("/exam/delete/{id}")
    @ApiOperation(value = "Delete exam")
    @ApiResponses(value = [
        ApiResponse(code = 204, message = "Successfully deleted exam"),
        ApiResponse(code = 404, message = "Exam not found", response = ErrorData::class),
        ApiResponse(code = 400, message = "Bad request", response = ErrorData::class)
    ])
    fun deleteExam(@PathVariable id: String, request: HttpServletRequest): ResponseEntity<String> {
        UUID.fromString(id)
        return proxyService.proxy(null, HttpMethod.DELETE, request,exam_host, exam_port!!,  "/api/v1/exam/remove/$id")
    }
}
