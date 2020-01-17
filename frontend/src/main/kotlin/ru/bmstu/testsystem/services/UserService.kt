package ru.bmstu.testsystem.services

import org.springframework.data.domain.Page
import ru.bmstu.testsystem.model.RegistrationData
import ru.bmstu.testsystem.model.UserData
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


interface UserService {
    fun getUsers(page: Int, limit: Int, request: HttpServletRequest, response: HttpServletResponse): Page<UserData>
    fun findById(id: String, request: HttpServletRequest, response: HttpServletResponse): UserData
    fun updateUser(newUserData: UserData, request: HttpServletRequest, response: HttpServletResponse): UserData?
    fun deleteUser(id: String, request: HttpServletRequest, response: HttpServletResponse)
    fun registerUser(rd: RegistrationData, request: HttpServletRequest, response: HttpServletResponse): UserData?
}
