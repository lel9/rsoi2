package ru.bmstu.testsystem.gateway.web

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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.client.RestOperations
import org.springframework.web.client.RestTemplate
import org.springframework.web.context.WebApplicationContext
import ru.bmstu.testsystem.gateway.TestingSystemApplication
import ru.bmstu.testsystem.gateway.model.UserAnswer
import ru.bmstu.testsystem.gateway.web.util.Utils
import java.net.URI

@RunWith(SpringRunner::class)
@SpringBootTest(classes = [TestingSystemApplication::class])
@AutoConfigureMockMvc
@WebAppConfiguration
class CompositRestApiProxyTest {
    @Autowired
    private lateinit var context: WebApplicationContext

    private lateinit var mvc: MockMvc
    private lateinit var mockRestServiceServer: MockRestServiceServer

    @Autowired
    private lateinit var restOperations: RestOperations

    private val examWithAns = "{\"id\":\"446ae2f3-eb60-44cb-b889-22f14ef06d82\"," +
            "\"name\":\"ename\",\"passes\":3,\"description\":\"edesc\"," +
            "\"questionIns\":[{\"id\":0,\"questionText\":\"1+1\"," +
            "\"type\":\"SINGLE_ANSWER\",\"variants\":[\"1\",\"2\"]," +
            "\"correctVariants\":[1],\"correctInputAnswer\":null}]}"

    private val ans: List<UserAnswer> = arrayListOf()

    private val results = "{\"content\":[{\"id\":\"18c4f984-22bd-4edd-ae66-6fb157328337\"," +
            "\"result\":\"11\",\"passedAt\":\"2007-11-18\"," + 
            "\"examId\":\"0596c2c0-a70a-47dd-81c8-31411a5b132a\"," + 
            "\"userId\":\"12412cdb-398f-4def-9cec-325b11968b56\"}]," + 
            "\"pageable\":{\"sort\": {\"sorted\":true,\"unsorted\":false,\"empty\":false}," +
            "\"pageSize\":12,\"pageNumber\":0,\"offset\":0,\"paged\":true,\"unpaged\":false}," +
            "\"totalPages\":1,\"totalElements\":1,\"last\":true,\"number\":0,\"size\":12,\"numberOfElements\":1," +
            "\"sort\":{\"sorted\":true,\"unsorted\":false,\"empty\":false},\"first\":true,\"empty\":false}\""

    private val oneExam = "{\"id\":\"0596c2c0-a70a-47dd-81c8-31411a5b132a\"," +
            "\"name\":\"string\",\"passes\":4,\"description\":\"string\"," +
            "\"questionIns\":[{\"id\":0,\"questionText\":\"string\"," +
            "\"type\":\"NO_ANSWER\",\"variants\":null}]}"


    private val oneUser = "{\"id\":\"12412cdb-398f-4def-9cec-325b11968b56\"," +
            "\"username\":\"admin\",\"email\":\"admin\",\"firstName\":\"admin\"," +
            "\"lastName\":\"admin\",\"birthday\":null}"

    private val compositResult = "{\"content\": [{\"id\":\"18c4f984-22bd-4edd-ae66-6fb157328337\"," +
            "\"result\":\"11\",\"passedAt\":\"2007-11-18\"," +
            "\"exam\":{\"id\":\"0596c2c0-a70a-47dd-81c8-31411a5b132a\",\"name\":\"string\"," +
            "\"passes\":4,\"description\":\"string\",\"questionIns\":[{\"id\":0," +
            "\"questionText\":\"string\",\"type\":\"NO_ANSWER\",\"variants\":null}]}," +
            "\"user\":{\"id\":\"12412cdb-398f-4def-9cec-325b11968b56\",\"username\":\"admin\"," +
            "\"email\":\"admin\",\"firstName\":\"admin\",\"lastName\":\"admin\",\"birthday\":null}}]," + 
            "\"pageable\":{\"sort\": {\"sorted\":true,\"unsorted\":false,\"empty\":false}," +
            "\"pageSize\":12,\"pageNumber\":0,\"offset\":0,\"paged\":true,\"unpaged\":false}," +
            "\"totalPages\":1,\"totalElements\":1,\"last\":true,\"number\":0,\"size\":12,\"numberOfElements\":1," +
            "\"sort\":{\"sorted\":true,\"unsorted\":false,\"empty\":false},\"first\":true,\"empty\":false}\""

    @Before
    fun setup() {
        mvc = MockMvcBuilders
            .webAppContextSetup(context)
            .build()
        mockRestServiceServer = MockRestServiceServer.createServer(restOperations as RestTemplate)

    }

    @Test
    fun deleteUser() {
        mockRestServiceServer.expect(
            MockRestRequestMatchers
                .requestTo("http://localhost:8081/api/v1/user/delete/74024847-db38-470b-b0ae-c02a865e13c9"))
            .andExpect(MockRestRequestMatchers.method(HttpMethod.DELETE))
            .andRespond(MockRestResponseCreators.withNoContent())

        mockRestServiceServer.expect(
            MockRestRequestMatchers
                .requestTo("http://localhost:8083/api/v1/result/delete/74024847-db38-470b-b0ae-c02a865e13c9"))
            .andExpect(MockRestRequestMatchers.method(HttpMethod.DELETE))
            .andRespond(MockRestResponseCreators.withNoContent())

        this.mvc.perform(Utils.makeDeleteRequest("/api/v1/gateway/user/delete/74024847-db38-470b-b0ae-c02a865e13c9"))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNoContent())
    }

    @Test
    fun addResult() {
        mockRestServiceServer.expect(
            MockRestRequestMatchers
                .requestTo("http://localhost:8082/api/v1/exam/inc/446ae2f3-eb60-44cb-b889-22f14ef06d82" +
                        "?examId=446ae2f3-eb60-44cb-b889-22f14ef06d82&userId=12412cdb-398f-4def-9cec-325b11968b56"))
            .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
            .andRespond(MockRestResponseCreators.withSuccess())

        mockRestServiceServer.expect(
            MockRestRequestMatchers
                .requestTo("http://localhost:8082/api/v1/exam/get/full/446ae2f3-eb60-44cb-b889-22f14ef06d82" +
                        "?examId=446ae2f3-eb60-44cb-b889-22f14ef06d82&userId=12412cdb-398f-4def-9cec-325b11968b56"))
            .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
            .andRespond(MockRestResponseCreators.withSuccess(examWithAns, MediaType.APPLICATION_JSON))

        mockRestServiceServer.expect(
            MockRestRequestMatchers
                .requestTo("http://localhost:8083/api/v1/result/add" +
                        "?examId=446ae2f3-eb60-44cb-b889-22f14ef06d82&userId=12412cdb-398f-4def-9cec-325b11968b56"))
            .andExpect(MockRestRequestMatchers.method(HttpMethod.POST))
            .andRespond(MockRestResponseCreators.withCreatedEntity(URI("")))

        this.mvc.perform(Utils
            .makePostRequest("/api/v1/gateway/result/add" +
                    "?examId=446ae2f3-eb60-44cb-b889-22f14ef06d82&userId=12412cdb-398f-4def-9cec-325b11968b56",
                ans))
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
    }

    @Test
    fun getResults() {
        mockRestServiceServer.expect(
            MockRestRequestMatchers
                .requestTo("http://localhost:8083/api/v1/result/get"))
            .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
            .andRespond(MockRestResponseCreators.withSuccess(results, MediaType.APPLICATION_JSON))

        mockRestServiceServer.expect(
            MockRestRequestMatchers
                .requestTo("http://localhost:8081/api/v1/user/get/12412cdb-398f-4def-9cec-325b11968b56"))
            .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
            .andRespond(MockRestResponseCreators.withSuccess(oneUser, MediaType.APPLICATION_JSON))

        mockRestServiceServer.expect(
            MockRestRequestMatchers
                .requestTo("http://localhost:8082/api/v1/exam/get/0596c2c0-a70a-47dd-81c8-31411a5b132a?ignore_deleted_flag=1"))
            .andExpect(MockRestRequestMatchers.method(HttpMethod.GET))
            .andRespond(MockRestResponseCreators.withSuccess(oneExam, MediaType.APPLICATION_JSON))
         
        this.mvc.perform(Utils.makeGetRequest("/api/v1/gateway/results"))
            .andDo(MockMvcResultHandlers.print())
            //.andExpect(content().json(compositResult))
            .andExpect(status().isOk())
    }
}
