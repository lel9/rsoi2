package ru.bmstu.testsystem.gateway.web

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.TestPropertySource
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import ru.bmstu.testsystem.gateway.TestingSystemApplication
import ru.bmstu.testsystem.gateway.model.RegistrationData
import ru.bmstu.testsystem.gateway.model.UserData
import ru.bmstu.testsystem.gateway.web.util.Utils
import org.mockito.InjectMocks
import org.springframework.web.client.RestTemplate
import org.mockito.Mock
import org.mockito.Mockito
import org.springframework.http.HttpMethod
import org.springframework.http.HttpRequest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.http.ResponseEntity
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers
import org.springframework.test.web.client.match.MockRestRequestMatchers.*
import org.springframework.test.web.client.response.MockRestResponseCreators.*
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.web.client.RestOperations
import ru.bmstu.testsystem.gateway.web.util.ProxyService


@RunWith(SpringRunner::class)
@SpringBootTest(classes = [TestingSystemApplication::class])
@AutoConfigureMockMvc
@WebAppConfiguration
class UsersRestApiProxyTest {

    @Autowired
    private lateinit var context: WebApplicationContext

    private lateinit var mvc: MockMvc
    private lateinit var mockRestServiceServer: MockRestServiceServer

    @Autowired
    private lateinit var restOperations: RestOperations

    private val allUsers = "[{\"id\":\"12412cdb-398f-4def-9cec-325b11968b56\"," +
            "\"username\":\"admin\",\"email\":\"admin\",\"firstName\":\"admin\"," +
            "\"lastName\":\"admin\",\"birthday\":null}," +
            "{\"id\":\"7c803c41-ca5f-4e66-9483-7e361db72917\",\"username\":\"admin2\"," +
            "\"email\":\"admin2\",\"firstName\":\"admin2\",\"lastName\":\"admin2\"," +
            "\"birthday\":null}]"

    private val oneUser = "{\"id\":\"12412cdb-398f-4def-9cec-325b11968b56\"," +
            "\"username\":\"admin\",\"email\":\"admin\",\"firstName\":\"admin\"," +
            "\"lastName\":\"admin\",\"birthday\":null}"

    private val userToAdd = RegistrationData("uname", "email")

    private val newUser = "{\"id\":\"12412cdb-398f-4def-9cec-325b11968b56\", \"username\":\"uname\",\"email\":\"email\"}"

    private val userToEdit = UserData("12412cdb-398f-4def-9cec-325b11968b56", "uname2", "email2")

    private val editedUser = "{\"id\":\"12412cdb-398f-4def-9cec-325b11968b56\", \"username\":\"uname2\",\"email\":\"email2\"}"

    @Before
    fun setup() {
        mvc = MockMvcBuilders
            .webAppContextSetup(context)
            .build()
        mockRestServiceServer = MockRestServiceServer.createServer(restOperations as RestTemplate)

    }

    @Test
    fun getAllUsers() {
        mockRestServiceServer.expect(requestTo("http://localhost:8081/api/v1/user/get"))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess(allUsers, APPLICATION_JSON))

        this.mvc.perform(MockMvcRequestBuilders.get("/api/v1/gateway/user/get"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(content().json(allUsers))
            .andExpect(status().isOk())
    }

    @Test
    fun getOneUser() {
        mockRestServiceServer.expect(requestTo("http://localhost:8081/api/v1/user/get/12412cdb-398f-4def-9cec-325b11968b56"))
            .andExpect(method(HttpMethod.GET))
            .andRespond(withSuccess(oneUser, APPLICATION_JSON))

        this.mvc.perform(MockMvcRequestBuilders.get("/api/v1/gateway/user/get/12412cdb-398f-4def-9cec-325b11968b56"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(content().json(oneUser))
            .andExpect(status().isOk())
    }

    @Test
    fun addUser() {
        mockRestServiceServer.expect(requestTo("http://localhost:8081/api/v1/user/register"))
            .andExpect(method(HttpMethod.POST))
            .andExpect(MockRestRequestMatchers.content().string(jacksonObjectMapper().writeValueAsString(userToAdd)))
            .andRespond(withSuccess(newUser, APPLICATION_JSON))

        this.mvc.perform(Utils.makePostRequest("/api/v1/gateway/user/register", userToAdd))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(content().string(newUser))
            .andExpect(status().isOk())
    }

    @Test
    fun editUser() {
        mockRestServiceServer.expect(requestTo("http://localhost:8081/api/v1/user/edit"))
            .andExpect(method(HttpMethod.POST))
            .andExpect(MockRestRequestMatchers.content().string(jacksonObjectMapper().writeValueAsString(userToEdit)))
            .andRespond(withSuccess(editedUser, APPLICATION_JSON))

        this.mvc.perform(Utils.makePostRequest("/api/v1/gateway/user/edit", userToEdit))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(content().string(editedUser))
            .andExpect(status().isOk())
    }

}
