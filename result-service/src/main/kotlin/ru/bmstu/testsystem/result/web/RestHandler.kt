package ru.bmstu.testsystem.result.web

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import ru.bmstu.testsystem.result.model.GenericResponse


@ControllerAdvice
class RestHandler : ResponseEntityExceptionHandler() {

    // 400

    @ExceptionHandler(value = [(IllegalArgumentException::class)])
    fun handleBadData(ex: IllegalArgumentException, request: WebRequest): ResponseEntity<Any> {
        val bodyOfResponse = GenericResponse(ex.message, "BadData")
        return handleExceptionInternal(ex, bodyOfResponse, HttpHeaders(), HttpStatus.BAD_REQUEST, request)
    }

    // 403
    @ExceptionHandler(value = [(AccessDeniedException::class)])
    fun handleAccessDenied(ex: AccessDeniedException, request: WebRequest): ResponseEntity<Any> {
        val bodyOfResponse = GenericResponse("Доступ запрещен", "AccessError")
        return handleExceptionInternal(ex, bodyOfResponse, HttpHeaders(), HttpStatus.FORBIDDEN, request)
    }

    @ExceptionHandler(value = [(Exception::class)])
    fun handleInternal(ex: Exception, request: WebRequest): ResponseEntity<Any> {
        ex.printStackTrace()
        val bodyOfResponse = GenericResponse("Внутренняя ошибка сервера", "InternalError")
        return ResponseEntity(bodyOfResponse, HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR)
    }

}
