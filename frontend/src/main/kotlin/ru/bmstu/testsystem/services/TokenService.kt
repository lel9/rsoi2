package ru.bmstu.testsystem.services

import ru.bmstu.testsystem.model.Oauth2TokenSimple
import javax.servlet.http.HttpServletResponse


interface TokenService {
    fun getAccessToken(code: String, response: HttpServletResponse)
    fun refreshToken(refreshToken: String?, response: HttpServletResponse): Oauth2TokenSimple?
    fun getUsername(): String?
}