package ru.bmstu.testsystem.gateway.web

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.swagger.annotations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.*
import org.springframework.web.bind.annotation.*
import ru.bmstu.testsystem.gateway.model.ErrorData
import ru.bmstu.testsystem.gateway.model.RegistrationData
import ru.bmstu.testsystem.gateway.model.UserData
import ru.bmstu.testsystem.gateway.web.util.CircuitBreaker
import ru.bmstu.testsystem.gateway.web.util.ProxyService
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid


@RestController
@RequestMapping("/api/v1/gateway")
@Api(value = "Testing system")
class UsersRestApiProxy {
    @Autowired
    private lateinit var proxyService: ProxyService

    @Autowired
    private lateinit var breaker: CircuitBreaker

    @GetMapping("/user/get")
    @ApiOperation(value = "Get all users", response = List::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved list")
    ])
    fun getUsers(@ApiParam(value = "page", required = false, defaultValue = "0")
                 @RequestParam(value = "page", required = false, defaultValue = "0") page: Int,
                 @ApiParam(value = "limit", required = false, defaultValue = "12")
                 @RequestParam(value = "limit", required = false, defaultValue = "12") limit: Int,
                 request: HttpServletRequest): ResponseEntity<String> {
        return breaker.action(proxyService, null, HttpMethod.GET, request, "localhost", 8081, "/api/v1/user/get")
    }

    @GetMapping("/user/get/{id}")
    @ApiOperation(value = "Get user by id", response = UserData::class)
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully retrieved user"),
        ApiResponse(code = 404, message = "User not found", response = ErrorData::class),
        ApiResponse(code = 400, message = "Bad request", response = ErrorData::class)
    ])
    fun getUserByName(@PathVariable id: String, request: HttpServletRequest): ResponseEntity<String> {
        UUID.fromString(id)
        return proxyService.proxy(null, HttpMethod.GET, request, "localhost", 8081, "/api/v1/user/get/$id")
    }

    @PostMapping("/user/register")
    @ApiOperation(value = "Add user")
    @ApiResponses(value = [
        ApiResponse(code = 201, message = "Successfully created user", response = UserData::class),
        ApiResponse(code = 400, message = "Bad request", response = ErrorData::class),
        ApiResponse(code = 409, message = "User already exists", response = ErrorData::class)
    ])
    fun registerUser(@ApiParam(value = "New user object", required = true)
                     @RequestBody @Valid body: RegistrationData, request: HttpServletRequest): ResponseEntity<String> {
        val string = jacksonObjectMapper().writeValueAsString(body)
        return proxyService.proxy(string, HttpMethod.POST, request, "localhost", 8081, "/api/v1/user/register")
    }

    @PostMapping("/user/edit")
    @ApiOperation(value = "Update user")
    @ApiResponses(value = [
        ApiResponse(code = 200, message = "Successfully updated user", response = UserData::class),
        ApiResponse(code = 400, message = "Bad request", response = ErrorData::class),
        ApiResponse(code = 409, message = "User already exists", response = ErrorData::class)
    ])
    fun editUser(@ApiParam(value = "Updated user object", required = true)
                 @RequestBody @Valid body: UserData, request: HttpServletRequest): ResponseEntity<String> {
        UUID.fromString(body.id)
        val string = jacksonObjectMapper().writeValueAsString(body)
        return proxyService.proxy(string, HttpMethod.POST, request, "localhost", 8081, "/api/v1/user/edit")
    }
}
