package ru.bmstu.testsystem.gateway.web

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.HttpMethod
import org.springframework.http.MediaType
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.client.MockRestServiceServer
import org.springframework.test.web.client.match.MockRestRequestMatchers
import org.springframework.test.web.client.response.MockRestResponseCreators
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.client.RestOperations
import org.springframework.web.client.RestTemplate
import org.springframework.web.context.WebApplicationContext
import ru.bmstu.testsystem.gateway.TestingSystemApplication
import ru.bmstu.testsystem.gateway.model.ExamDataIn
import ru.bmstu.testsystem.gateway.model.RegistrationData
import ru.bmstu.testsystem.gateway.model.UserData
import ru.bmstu.testsystem.gateway.web.util.Utils


@RunWith(SpringRunner::class)
@SpringBootTest(classes = [TestingSystemApplication::class])
@AutoConfigureMockMvc
@WebAppConfiguration
class ExamsRestApiProxyTest {

    @Autowired
    private lateinit var context: WebApplicationContext

    private lateinit var mvc: MockMvc
    private lateinit var mockRestServiceServer: MockRestServiceServer

    @Autowired
    private lateinit var restOperations: RestOperations

    private val allExams = "[{\"id\":\"74024847-db38-470b-b0ae-c02a865e13c9\"," +
            "\"name\":\"string\",\"passes\":4,\"description\":\"string\"," +
            "\"questionIns\":[{\"id\":0,\"questionText\":\"string\"," +
            "\"type\":\"NO_ANSWER\",\"variants\":null}]}," +
            "{\"id\":\"e2eea816-03e1-487c-96a1-ddc88c3aa513\",\"name\":\"string\"," +
            "\"passes\":0,\"description\":\"string\",\"questionIns\":[{\"id\":0," +
            "\"questionText\":\"string\",\"type\":\"NO_ANSWER\",\"variants\":null}]}]"

    private val oneExam = "{\"id\":\"74024847-db38-470b-b0ae-c02a865e13c9\"," +
            "\"name\":\"string\",\"passes\":4,\"description\":\"string\"," +
            "\"questionIns\":[{\"id\":0,\"questionText\":\"string\"," +
            "\"type\":\"NO_ANSWER\",\"variants\":null}]}"

    private val examToAdd = ExamDataIn("ename", "desc", arrayListOf())

    private val newExam = "{\"id\":\"12412cdb-398f-4def-9cec-325b11968b56\", " +
            "\"name\":\"ename\",\"description\":\"desc\", \"passes\": 0, \"questionIns\": []}"

    @Before
    fun setup() {
        mvc = MockMvcBuilders
            .webAppContextSetup(context)
            .build()
        mockRestServiceServer = MockRestServiceServer.createServer(restOperations as RestTemplate)

    }

    @Test
    fun getAllExams() {
        mockRestServiceServer.expect(MockRestRequestMatchers.requestTo("http://localhost:8082/api/v1/exam/get"))
            .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
            .andRespond(MockRestResponseCreators.withSuccess(allExams, MediaType.APPLICATION_JSON))

        this.mvc.perform(MockMvcRequestBuilders.get("/api/v1/gateway/exam/get"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.content().json(allExams))
            .andExpect(MockMvcResultMatchers.status().isOk())
    }

    @Test
    fun getOneExam() {
        mockRestServiceServer.expect(MockRestRequestMatchers
            .requestTo("http://localhost:8082/api/v1/exam/get/74024847-db38-470b-b0ae-c02a865e13c9"))
            .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
            .andRespond(MockRestResponseCreators.withSuccess(oneExam, MediaType.APPLICATION_JSON))

        this.mvc.perform(MockMvcRequestBuilders.get("/api/v1/gateway/exam/get/74024847-db38-470b-b0ae-c02a865e13c9"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.content().json(oneExam))
            .andExpect(MockMvcResultMatchers.status().isOk())
    }

    @Test
    fun addExam() {
        mockRestServiceServer.expect(MockRestRequestMatchers.requestTo("http://localhost:8082/api/v1/exam/add"))
            .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
            .andExpect(MockRestRequestMatchers.content().string(jacksonObjectMapper().writeValueAsString(examToAdd)))
            .andRespond(MockRestResponseCreators.withSuccess(newExam, MediaType.APPLICATION_JSON))

        this.mvc.perform(Utils.makePostRequest("/api/v1/gateway/exam/add", examToAdd))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.content().string(newExam))
            .andExpect(MockMvcResultMatchers.status().isOk())
    }

    @Test
    fun deleteExam() {
        mockRestServiceServer.expect(MockRestRequestMatchers
            .requestTo("http://localhost:8082/api/v1/exam/remove/74024847-db38-470b-b0ae-c02a865e13c9"))
            .andExpect(MockRestRequestMatchers.method(HttpMethod.DELETE))
            .andRespond(MockRestResponseCreators.withNoContent())

        this.mvc.perform(Utils.makeDeleteRequest("/api/v1/gateway/exam/delete/74024847-db38-470b-b0ae-c02a865e13c9"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNoContent())
    }

}
