package ru.bmstu.testsystem.users.web.handler

import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter
import ru.bmstu.testsystem.users.model.resolve
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class TokenValidationHandler(var host: String, var port: Int, var cipher: Cipher): HandlerInterceptorAdapter() {

    override fun preHandle(request: HttpServletRequest, response: HttpServletResponse, handler: Any): Boolean {
        if (request.requestURI == "/api/v1/token")
            return true

        val header = request.getHeader(HttpHeaders.AUTHORIZATION)
        if (header == null || header.isEmpty()) {
            response.status = HttpStatus.UNAUTHORIZED.value()
            return false
        }
        val credential = resolve(host, port)!!
        cipher.init(Cipher.DECRYPT_MODE, SecretKeySpec(Base64.getDecoder().decode(credential.appSecret.toByteArray()), "AES"));
        val plainText = Base64.getDecoder().decode(header)
        val cipherText = String(cipher.doFinal(plainText))
        val split = cipherText.split(":")
        try {
            if (split[0].toLong() > System.currentTimeMillis())
                return true
        } catch (e: Exception) {}

        response.status = HttpStatus.UNAUTHORIZED.value()
        return false
    }
}