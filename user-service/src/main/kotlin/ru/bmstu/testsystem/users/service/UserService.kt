package ru.bmstu.testsystem.users.service

import ru.bmstu.testsystem.users.model.RegistrationData
import ru.bmstu.testsystem.users.model.UserData
import java.util.*


interface UserService {

    fun registerUser(registrationData: RegistrationData): UserData?

    fun updateUser(newUserData: UserData): UserData
    fun getAllUsers(page: Int, limit: Int): List<UserData>
    fun deleteUser(id: UUID)
    fun findById(id: UUID): UserData
}
