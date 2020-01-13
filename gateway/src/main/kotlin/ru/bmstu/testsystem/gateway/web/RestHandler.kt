package ru.bmstu.testsystem.gateway.web

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import ru.bmstu.testsystem.gateway.model.GenericResponse
import javax.security.auth.login.CredentialExpiredException
import javax.security.auth.login.CredentialNotFoundException

@ControllerAdvice
class RestHandler : ResponseEntityExceptionHandler() {

    // 400
    override protected fun handleMethodArgumentNotValid(ex: MethodArgumentNotValidException,
                                                        headers:HttpHeaders,
                                                        status:HttpStatus,
                                                        request:WebRequest):ResponseEntity<Any> {
        val result = ex.getBindingResult()
        val bodyOfResponse = GenericResponse(result.getAllErrors(), "Invalid" + result.getObjectName())
        return handleExceptionInternal(ex, bodyOfResponse, HttpHeaders(), HttpStatus.BAD_REQUEST, request)
    }

    @ExceptionHandler(value = [(IllegalArgumentException::class)])
    fun handleBadData(ex: IllegalArgumentException, request: WebRequest): ResponseEntity<Any> {
        val bodyOfResponse = GenericResponse(ex.message, "BadData")
        return handleExceptionInternal(ex, bodyOfResponse, HttpHeaders(), HttpStatus.BAD_REQUEST, request)
    }

    //401
    @ExceptionHandler(value = [(CredentialNotFoundException::class)])
    fun handleCredentialNotFound(ex: CredentialNotFoundException, request: WebRequest): ResponseEntity<Any> {
        val bodyOfResponse = GenericResponse(ex.message, "CredentialNotFound")
        return handleExceptionInternal(ex, bodyOfResponse, HttpHeaders(), HttpStatus.UNAUTHORIZED, request)
    }

    @ExceptionHandler(value = [(CredentialExpiredException::class)])
    fun handleCredentialExpired(ex: CredentialExpiredException, request: WebRequest): ResponseEntity<Any> {
        val bodyOfResponse = GenericResponse(ex.message, "CredentialExpired")
        return handleExceptionInternal(ex, bodyOfResponse, HttpHeaders(), HttpStatus.UNAUTHORIZED, request)
    }

    //500
    @ExceptionHandler(value = [(ResourceAccessException::class)])
    fun handleTimedOut(ex: ResourceAccessException, request: WebRequest): ResponseEntity<Any> {
        val bodyOfResponse = GenericResponse(ex.message, "TimeOut")
        return handleExceptionInternal(ex, bodyOfResponse, HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request)
    }

    @ExceptionHandler(value = [(Exception::class)])
    fun handleInternal(ex: Exception, request: WebRequest): ResponseEntity<Any> {
        ex.printStackTrace()
        val bodyOfResponse = GenericResponse("Внутренняя ошибка сервера", "InternalError")
        return ResponseEntity(bodyOfResponse, HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR)
    }

}
