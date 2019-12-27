package ru.bmstu.testsystem.result.web

import org.hamcrest.Matchers
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
import org.springframework.test.web.servlet.result.MockMvcResultHandlers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.context.WebApplicationContext
import ru.bmstu.testsystem.result.TestingSystemApplication
import ru.bmstu.testsystem.result.model.QuestionDataWithAnsOut
import ru.bmstu.testsystem.result.model.UserAnswer
import ru.bmstu.testsystem.result.model.UserAnswers
import ru.bmstu.testsystem.result.web.util.Utils

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

    @Before
    fun setup() {
        mvc = MockMvcBuilders
            .webAppContextSetup(context)
            .build()
    }

    @Test
    fun addResGood() {
        val questions = arrayListOf<QuestionDataWithAnsOut>()
        questions.add(QuestionDataWithAnsOut())
        val answers = arrayListOf<UserAnswer>()
        answers.add(UserAnswer())

        this.mvc.perform(
            Utils.makePostRequest("/api/v1/result/add?" +
                    "examId=12412cdb-398f-4def-9cec-325b11968b56&userId=833f9d04-9d81-4983-9dae-b69a89d9efe7",
                UserAnswers(questions, answers))
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isCreated)
    }

    @Test
    fun addResBad() {
        val questions = arrayListOf<QuestionDataWithAnsOut>()
        questions.add(QuestionDataWithAnsOut())

        this.mvc.perform(
            Utils.makePostRequest("/api/v1/result/add?" +
                    "examId=12412cdb-398f-4def-9cec-325b11968b56&userId=833f9d04-9d81-4983-9dae-b69a89d9efe7", UserAnswers(questions, null))
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun addResBad2() {
        this.mvc.perform(
            Utils.makePostRequest("/api/v1/result/add?" +
                    "examId=12412cdb-398f-4def-9cec-325b11968b56&userId=833f9d04-9d81-4983-9dae-b69a89d9efe7",
                UserAnswers(null, null))
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun deleteResultGood() {
        this.mvc.perform(
            Utils.makeDeleteRequest("/api/v1/result/delete/12412cdb-398f-4def-9cec-325b11968b56")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNoContent)
    }

    @Test
    fun deleteResultBad2() {
        this.mvc.perform(
            Utils.makeDeleteRequest("/api/v1/result/delete/111")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun get() {
        this.mvc.perform(
            Utils.makeGetRequest("/api/v1/result/get?page=0&limit=2")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            //.andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize<Any>(2)))
    }
}
