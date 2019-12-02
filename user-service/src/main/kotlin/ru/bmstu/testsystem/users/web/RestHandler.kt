package ru.bmstu.testsystem.users.web

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import ru.bmstu.testsystem.users.exception.UserAlreadyExistsException

import ru.bmstu.testsystem.users.exception.NoUserException
import ru.bmstu.testsystem.users.model.GenericResponse

@ControllerAdvice
class RestHandler : ResponseEntityExceptionHandler() {

    // 400
    @ExceptionHandler(value = [(NoUserException::class)])
    fun handleNoSuchUser(ex: NoUserException, request: WebRequest): ResponseEntity<Any> {
        val bodyOfResponse = GenericResponse(ex.message, "NoSuchUser")
        return handleExceptionInternal(ex, bodyOfResponse, HttpHeaders(), HttpStatus.NOT_FOUND, request)
    }

    @ExceptionHandler(value = [(IllegalArgumentException::class)])
    fun handleBadData(ex: IllegalArgumentException, request: WebRequest): ResponseEntity<Any> {
        val bodyOfResponse = GenericResponse(ex.message, "BadUserData")
        return handleExceptionInternal(ex, bodyOfResponse, HttpHeaders(), HttpStatus.BAD_REQUEST, request)
    }

    // 403
    @ExceptionHandler(value = [(AccessDeniedException::class)])
    fun handleAccessDenied(ex: AccessDeniedException, request: WebRequest): ResponseEntity<Any> {
        val bodyOfResponse = GenericResponse("Доступ запрещен", "AccessError")
        return handleExceptionInternal(ex, bodyOfResponse, HttpHeaders(), HttpStatus.FORBIDDEN, request)
    }

    // 409
    @ExceptionHandler(value = [(UserAlreadyExistsException::class)])
    fun handleUserAlreadyExist(ex: UserAlreadyExistsException, request: WebRequest): ResponseEntity<Any> {
        val bodyOfResponse = GenericResponse(ex.message, "UserAlreadyExists")
        return handleExceptionInternal(ex, bodyOfResponse, HttpHeaders(), HttpStatus.CONFLICT, request)
    }

    @ExceptionHandler(value = [(Exception::class)])
    fun handleInternal(ex: Exception, request: WebRequest): ResponseEntity<Any> {
        ex.printStackTrace()
        val bodyOfResponse = GenericResponse("Внутренняя ошибка сервера", "InternalError")
        return ResponseEntity(bodyOfResponse, HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR)
    }

}
