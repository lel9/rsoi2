package ru.bmstu.testsystem.controller

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.propertyeditors.CustomDateEditor
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.WebDataBinder
import org.springframework.web.bind.annotation.*
import org.springframework.web.bind.annotation.ExceptionHandler
import ru.bmstu.testsystem.model.UserData
import ru.bmstu.testsystem.services.UserServiceImpl
import java.text.SimpleDateFormat
import java.util.*
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Controller
class EditProfile {

    @Autowired
    private lateinit var userService: UserServiceImpl

    @InitBinder
    fun initBinder(binder: WebDataBinder) {
        binder.registerCustomEditor(
                Date::class.java, CustomDateEditor(SimpleDateFormat("yyyy-MM-dd"), true)
        )
    }

    @GetMapping("/edit_profile/{id}")
    fun getEditProfile(@PathVariable id: String, request: HttpServletRequest, response: HttpServletResponse,
                       model: Model): String {
        val user = userService.findById(id, request, response)
        model.addAttribute("user", user)
        return "edit_profile"
    }

    @PostMapping("/edit_profile")
    fun postEditProfile(@ModelAttribute userData: UserData,
                        request: HttpServletRequest, response: HttpServletResponse,
                        model: Model): String {
        val updateUser = userService.updateUser(userData, request, response)
        if (updateUser == null) {
            model.addAttribute("user", userData)
            return "redirect:/edit_profile/${userData.id}?exist=true"
        }
        model.addAttribute("user", updateUser)
        return "edit_profile"
    }

}
