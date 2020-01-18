package ru.bmstu.testsystem.users.web

import ru.bmstu.testsystem.users.TestingSystemApplication
import org.hamcrest.Matchers
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.context.WebApplicationContext
import ru.bmstu.testsystem.users.model.RegistrationData
import ru.bmstu.testsystem.users.model.UserData
import ru.bmstu.testsystem.users.model.AppCredential
import ru.bmstu.testsystem.users.web.util.Utils
import java.util.*

@RunWith(SpringRunner::class)
@TestPropertySource(locations=["classpath:test.properties"])
@SpringBootTest(classes = [TestingSystemApplication::class])
@AutoConfigureMockMvc
@WebAppConfiguration
@Transactional
class RestApiImplTest {

    @Autowired
    private lateinit var context: WebApplicationContext

    private lateinit var mvc: MockMvc

    private var token: String = ""

    @Before
    fun setup() {
        mvc = MockMvcBuilders
            .webAppContextSetup(context)
            .build()

        token = this.mvc.perform(
            Utils.makePostRequest("/api/v1/token", AppCredential("userService", "vbwEAD63e0NPL1IbfzaXfg=="), token))
                 .andReturn()
                 .getResponse().contentAsString
    }

    @Test
    fun signUpGood() {
        this.mvc.perform(
            Utils.makePostRequest("/api/v1/user/register", RegistrationData("uname", "email"), token)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isCreated)
    }

    @Test
    fun signUpExist() {
        this.mvc.perform(
            Utils.makePostRequest("/api/v1/user/register", RegistrationData("admin", "email"), token)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isConflict)
    }

    @Test
    fun getUserGood() {
        this.mvc.perform(
            Utils.makeGetRequest("/api/v1/user/get/12412cdb-398f-4def-9cec-325b11968b56", token)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun getUserBad() {
        this.mvc.perform(
            Utils.makeGetRequest("/api/v1/user/get/12412cdb-398f-4def-9cec-325b11968b57", token)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun getUserBad2() {
        this.mvc.perform(
            Utils.makeGetRequest("/api/v1/user/get/123", token)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun deleteUserGood() {
        this.mvc.perform(
            Utils.makeDeleteRequest("/api/v1/user/delete/12412cdb-398f-4def-9cec-325b11968b56", token)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNoContent)
    }

    @Test
    fun deleteUserBad() {
        this.mvc.perform(
            Utils.makeDeleteRequest("/api/v1/user/delete/12412cdb-398f-4def-9cec-325b11968b57", token)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun deleteUserBad2() {
        this.mvc.perform(
            Utils.makeDeleteRequest("/api/v1/user/delete/111", token)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun editUserGood() {
        this.mvc.perform(
            Utils.makePostRequest("/api/v1/user/edit", UserData("12412cdb-398f-4def-9cec-325b11968b56", "newuname", "email"), token)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun editUserBad() {
        this.mvc.perform(
            Utils.makePostRequest("/api/v1/user/edit", UserData("12412cdb-398f-4def-9cec-325b11968b56", "user", "email"), token)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isConflict)
    }

    @Test
    fun editUserBad2() {
        this.mvc.perform(
            Utils.makePostRequest("/api/v1/user/edit", UserData("12412cdb-398f-4def-9cec-325b11968b57", "user", "email"), token)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun editUserBad3() {
        this.mvc.perform(
            Utils.makePostRequest("/api/v1/user/edit", UserData("1", "user", "email"), token)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun get() {
        this.mvc.perform(
            Utils.makeGetRequest("/api/v1/user/get?page=0&limit=2", token)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            //.andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize<Any>(2)))
    }
}
