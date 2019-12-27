package ru.bmstu.testsystem.exams.web

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import ru.bmstu.testsystem.exams.exception.NoExamException
import ru.bmstu.testsystem.exams.model.GenericResponse


@ControllerAdvice
class RestHandler : ResponseEntityExceptionHandler() {

    // 400
    override protected fun handleMethodArgumentNotValid(ex:MethodArgumentNotValidException,
                                               headers:HttpHeaders,
                                               status:HttpStatus,
                                               request:WebRequest):ResponseEntity<Any> {
        val result = ex.getBindingResult()
        val bodyOfResponse = GenericResponse(result.getAllErrors(), "Invalid" + result.getObjectName())
        return handleExceptionInternal(ex, bodyOfResponse, HttpHeaders(), HttpStatus.BAD_REQUEST, request)
    }

    @ExceptionHandler(value = [(NoExamException::class)])
    fun handleNoSuchUser(ex: NoExamException, request: WebRequest): ResponseEntity<Any> {
        val bodyOfResponse = GenericResponse(ex.message, "NoSuchExam")
        return handleExceptionInternal(ex, bodyOfResponse, HttpHeaders(), HttpStatus.NOT_FOUND, request)
    }

    @ExceptionHandler(value = [(IllegalArgumentException::class)])
    fun handleBadData(ex: IllegalArgumentException, request: WebRequest): ResponseEntity<Any> {
        val bodyOfResponse = GenericResponse(ex.message, "BadExamData")
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
        val bodyOfResponse = GenericResponse(ex.message, "InternalError")
        return ResponseEntity(bodyOfResponse, HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR)
    }

}
