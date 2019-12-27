package ru.bmstu.testsystem.exams.web

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
import ru.bmstu.testsystem.exams.TestingSystemApplication
import ru.bmstu.testsystem.exams.domain.QuestionType
import ru.bmstu.testsystem.exams.model.ExamDataIn
import ru.bmstu.testsystem.exams.model.QuestionDataIn
import ru.bmstu.testsystem.exams.web.util.Utils

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
    fun addExamBad() {
        this.mvc.perform(
            Utils.makePostRequest("/api/v1/exam/add", ExamDataIn("name", "desc", arrayListOf()))
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun addExamGood() {
        var questions = arrayListOf<QuestionDataIn>()
        questions.add(QuestionDataIn("text", QuestionType.NO_ANSWER, correctInputAnswer = "ans"))
        this.mvc.perform(
            Utils.makePostRequest("/api/v1/exam/add", ExamDataIn("name", "desc", questions))
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isCreated)
    }

    @Test
    fun incPassedGood() {
        this.mvc.perform(
            Utils.makePostRequest("/api/v1/exam/inc/0596c2c0-a70a-47dd-81c8-31411a5b132a", null)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun incPassedBad() {
        this.mvc.perform(
            Utils.makePostRequest("/api/v1/exam/inc/0596c2c0-a70a-47dd-81c8-31411a5b132b", null)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun incPassedBad2() {
        this.mvc.perform(
            Utils.makePostRequest("/api/v1/exam/inc/11", null)
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun getExamGood() {
        this.mvc.perform(
            Utils.makeGetRequest("/api/v1/exam/get/0596c2c0-a70a-47dd-81c8-31411a5b132a")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun getExamBad() {
        this.mvc.perform(
            Utils.makeGetRequest("/api/v1/exam/get/0596c2c0-a70a-47dd-81c8-31411a5b132b")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun getExamBad2() {
        this.mvc.perform(
            Utils.makeGetRequest("/api/v1/exam/get/123")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun getExamFullGood() {
        this.mvc.perform(
            Utils.makeGetRequest("/api/v1/exam/get/full/0596c2c0-a70a-47dd-81c8-31411a5b132a")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
    }

    @Test
    fun getExamFullBad() {
        this.mvc.perform(
            Utils.makeGetRequest("/api/v1/exam/get/full/0596c2c0-a70a-47dd-81c8-31411a5b132b")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun getExamFullBad2() {
        this.mvc.perform(
            Utils.makeGetRequest("/api/v1/exam/get/full/123")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun deleteExamGood() {
        this.mvc.perform(
            Utils.makeDeleteRequest("/api/v1/exam/remove/0596c2c0-a70a-47dd-81c8-31411a5b132a")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNoContent)
    }

    @Test
    fun deleteExamBad() {
        this.mvc.perform(
            Utils.makeDeleteRequest("/api/v1/exam/remove/0596c2c0-a70a-47dd-81c8-31411a5b132b")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isNotFound)
    }

    @Test
    fun deleteExamBad2() {
        this.mvc.perform(
            Utils.makeDeleteRequest("/api/v1/exam/remove/111")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun get() {
        this.mvc.perform(
            Utils.makeGetRequest("/api/v1/exam/get?page=0&limit=2")
        )
            .andDo(MockMvcResultHandlers.print())
            .andExpect(MockMvcResultMatchers.status().isOk)
            //.andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.hasSize<Any>(2)))
    }
}
