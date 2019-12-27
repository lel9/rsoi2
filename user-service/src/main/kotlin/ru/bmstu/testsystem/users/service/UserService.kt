package ru.bmstu.testsystem.users.service

import org.springframework.data.domain.Page
import ru.bmstu.testsystem.users.model.RegistrationData
import ru.bmstu.testsystem.users.model.UserData
import java.util.*


interface UserService {

    fun registerUser(registrationData: RegistrationData): UserData?

    fun updateUser(newUserData: UserData): UserData
    fun getAllUsers(page: Int, limit: Int): Page<UserData>
    fun deleteUser(id: UUID)
    fun findById(id: UUID): UserData
}
