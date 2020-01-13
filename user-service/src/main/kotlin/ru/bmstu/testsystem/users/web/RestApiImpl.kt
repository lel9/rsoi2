package ru.bmstu.testsystem.users.web

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.propertyeditors.CustomDateEditor
import org.springframework.data.domain.Page
import org.springframework.http.HttpStatus
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.*
import ru.bmstu.testsystem.users.model.RegistrationData
import ru.bmstu.testsystem.users.model.UserData
import ru.bmstu.testsystem.users.service.UserServiceImpl
import java.text.DateFormat
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("/api/v1/user")
class RestApiImpl {

    @Autowired
    private lateinit var userService: UserServiceImpl
    
    @InitBinder
    fun initBinder(binder: WebDataBinder) {
        binder.registerCustomEditor(
                Date::class.java, CustomDateEditor(DateFormat.getInstance(), true)
        )
    }

    @GetMapping("/get/{id}")
    @ResponseStatus(HttpStatus.OK)
    fun getUser(@PathVariable id: String): UserData {
        val user = userService.findById(UUID.fromString(id))
        return user
    }

    @GetMapping("/get")
    @ResponseStatus(HttpStatus.OK)
    fun getAllUsers(@RequestParam(value = "page", defaultValue = "0", required = false) page: Int,
                    @RequestParam(value = "limit", defaultValue = "12", required = false) limit: Int): Page<UserData> {
        return userService.getAllUsers(page, limit)
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    fun registerUser(@RequestBody @Valid rd: RegistrationData): UserData {
        val user = userService.registerUser(rd)
        return user
    }

    @PostMapping("/edit")
    @ResponseStatus(HttpStatus.OK)
    fun editProfile(@RequestBody @Valid userData: UserData): UserData {
        val updatedUser = userService.updateUser(userData)
        return updatedUser
    }

    @DeleteMapping("/delete/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteUser(@PathVariable id: String) {
        userService.deleteUser(UUID.fromString(id))
    }

}
