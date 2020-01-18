package ru.bmstu.testsystem.users.web.util

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.util.MultiValueMap

import java.nio.charset.Charset

object Utils {

    val APPLICATION_JSON_UTF8 =
        MediaType(MediaType.APPLICATION_JSON.type, MediaType.APPLICATION_JSON.subtype, Charset.forName("utf8"))

    @Throws(JsonProcessingException::class)
    fun makePostRequest(route: String, body: Any, token: String): MockHttpServletRequestBuilder {
        return MockMvcRequestBuilders
            .post(route)
            .header("Authorization", token)
            .contentType(APPLICATION_JSON_UTF8)
            .content(makeRequestBody(body))
    }

    fun makeGetRequest(route: String, token: String): MockHttpServletRequestBuilder {
        return MockMvcRequestBuilders
            .get(route)
            .header("Authorization", token)
    }

    fun makeDeleteRequest(route: String, token: String): MockHttpServletRequestBuilder {
        return MockMvcRequestBuilders
            .delete(route)
            .header("Authorization", token)
    }

    fun makeMultipartRequest(
        route: String,
        multipartFile: MockMultipartFile,
        params: MultiValueMap<String, String>
    ): MockHttpServletRequestBuilder {
        return MockMvcRequestBuilders
            .multipart(route)
            .file(multipartFile)
            .params(params)
            .contentType("multipart/form-data")
    }

    @Throws(JsonProcessingException::class)
    fun makeRequestBody(`object`: Any): String {
        val mapper = ObjectMapper()
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
        return mapper.writeValueAsString(`object`)
    }
}
