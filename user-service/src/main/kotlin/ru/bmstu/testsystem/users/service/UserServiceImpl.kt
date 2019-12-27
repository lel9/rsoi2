package ru.bmstu.testsystem.users.service

import ru.bmstu.testsystem.users.domain.Person
import ru.bmstu.testsystem.users.domain.User
import ru.bmstu.testsystem.users.exception.NoUserException
import ru.bmstu.testsystem.users.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.data.domain.Page
import ru.bmstu.testsystem.users.exception.UserAlreadyExistsException
import ru.bmstu.testsystem.users.model.RegistrationData
import ru.bmstu.testsystem.users.model.UserData
import java.util.*
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort


@Service("userService")
class UserServiceImpl : UserService {

    @Autowired
    private lateinit var userRepository: UserRepository

    override fun findById(id: UUID): UserData {
        val byId = userRepository.findById(id)
        if (!byId.isPresent)
            throw NoUserException()
        return UserData(byId.get())
    }

    override fun getAllUsers(page: Int, limit: Int): Page<UserData> {
        val pageableRequest = PageRequest.of(page, limit, Sort.by(Sort.Direction.ASC, "username"))
        val users = userRepository.findAll(pageableRequest)

        //val list = ArrayList<UserData>()
        //users.forEach { user ->
        //    list.add(
        //        UserData(user)
        //    )
        //}
        return users.map { user -> UserData(user) }
    }

    override fun registerUser(registrationData: RegistrationData): UserData {
        if (registrationData.email == "" ||
                registrationData.username == "")
            throw IllegalArgumentException("Email or username is empty")

        val user = userRepository.findByUsername(registrationData.username!!)
        if (user != null) {
            throw UserAlreadyExistsException()
        }

        val newUser = User(registrationData.username!!, registrationData.email!!)
        val saved = userRepository.save(newUser)
        return UserData(saved)
    }

    override fun updateUser(newUserData: UserData): UserData {
        val byId = userRepository.findById(UUID.fromString(newUserData.id))
        if (!byId.isPresent)
            throw NoUserException()

        val user = byId.get()

        if (newUserData.email == "" || newUserData.username == "")
            throw IllegalArgumentException("Email or username is empty")

        if (user.username != newUserData.username) {
            val exists = userRepository.findByUsername(newUserData.username!!)
            if (exists != null)
                throw UserAlreadyExistsException()
            user.username = newUserData.username!!
        }

        if (user.email != newUserData.email)
            user.email = newUserData.email!!

        val newPerson = Person(newUserData.firstName, newUserData.lastName, newUserData.birthday)
        val oldPerson = user.person

        if (oldPerson.firstName != newPerson.firstName)
            oldPerson.firstName = newPerson.firstName

        if (oldPerson.lastName != newPerson.lastName)
            oldPerson.lastName = newPerson.lastName

        if (oldPerson.birthday == null || oldPerson.birthday != newPerson.birthday)
            oldPerson.birthday = newPerson.birthday

        userRepository.save(user)
        return UserData(user)
    }

    override fun deleteUser(id: UUID) {
        if (!userRepository.findById(id).isPresent)
            throw NoUserException()
        userRepository.deleteById(id)
    }
}
