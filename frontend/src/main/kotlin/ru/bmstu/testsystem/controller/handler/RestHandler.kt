package ru.bmstu.testsystem.controller.handler

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.client.HttpStatusCodeException
import org.springframework.web.client.ResourceAccessException
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler
import ru.bmstu.testsystem.model.ErrorData
import ru.bmstu.testsystem.services.ProxyService
import javax.security.auth.login.CredentialExpiredException
import javax.security.auth.login.CredentialNotFoundException
import javax.servlet.http.HttpServletRequest

@ControllerAdvice
class RestHandler : ResponseEntityExceptionHandler() {
    @Value("\${spring.security.oauth2.provider.authorization-uri}")
    lateinit var authorization: String

    @Value("\${spring.security.oauth2.client.client-id}")
    lateinit var client: String

    @Value("\${spring.security.oauth2.client.scope}")
    lateinit var scope: String

    @Value("\${spring.security.oauth2.client.redirect-uri-template}")
    lateinit var redirect: String

    @Autowired
    lateinit var proxyService: ProxyService

    // 400
    @ExceptionHandler(value = [(HttpStatusCodeException::class)])
    fun handlerAnnotationException(exception: HttpStatusCodeException, request: HttpServletRequest, model: Model) : String {
        var res = "error_page"
        try {
            val errorData = jacksonObjectMapper().readValue<ErrorData>(exception.responseBodyAsString)
            if (errorData.type.equals("TimeOut")) {
                model.addAttribute("info", "Сервис временно недоступен")
            }
            else if (errorData.type.equals("CredentialNotFound")) {
                //http://localhost:8085/auth/oauth/authorize?response_type=code&client_id=SampleClientId&scope=user_info&redirect_uri=http%3A%2F%2Flocalhost%3A3000%2Flogin%2Foauth2%2Fcode%2F
                res = "redirect:$authorization?response_type=code&client_id=$client&scope=$scope&redirect_uri=$redirect"
            }
            else
                model.addAttribute("info", errorData.message)
        } catch (ex: Exception) {
            model.addAttribute("info", exception.statusCode)
        }
        return res
    }

    // 400
    @ExceptionHandler(value = [(CredentialNotFoundException::class)])
    fun handlerCredentialNotFoundException(exception: CredentialNotFoundException, request: HttpServletRequest, model: Model) : String {
        return  "redirect:$authorization?response_type=code&client_id=$client&scope=$scope&state=${request.requestURI}&redirect_uri=$redirect"
    }

    // 500
    @ExceptionHandler(value = [(ResourceAccessException::class)])
    fun handleResourceAccessException(exception: ResourceAccessException, model: Model): String {
        //return "redirect:$authorization?response_type=code&client_id=$client&scope=$scope&redirect_uri=$redirect"
        model.addAttribute("info", "Сервис временно недоступен")
        exception.printStackTrace()
        return "error_page"
    }

    @ExceptionHandler(value = [(Exception::class)])
    fun handleInternal(exception: Exception, model: Model): String {
        model.addAttribute("info", "Неизвестная ошибка")
        return "error_page"
    }

}
