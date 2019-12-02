package ru.bmstu.testsystem.result.web.util

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.http.MediaType
import org.springframework.mock.web.MockMultipartFile
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.util.MultiValueMap

import java.nio.charset.Charset

object Utils {

    val APPLICATION_JSON_UTF8 =
        MediaType(MediaType.APPLICATION_JSON.type, MediaType.APPLICATION_JSON.subtype, Charset.forName("utf8"))

    @Throws(JsonProcessingException::class)
    fun makePostRequest(route: String, body: Any): MockHttpServletRequestBuilder {
        return MockMvcRequestBuilders
            .post(route)
            .with(SecurityMockMvcRequestPostProcessors.csrf().asHeader())
            .contentType(APPLICATION_JSON_UTF8)
            .content(makeRequestBody(body))
    }

    fun makeGetRequest(route: String): MockHttpServletRequestBuilder {
        return MockMvcRequestBuilders
            .get(route)
            .with(SecurityMockMvcRequestPostProcessors.csrf().asHeader())
    }

    fun makeDeleteRequest(route: String): MockHttpServletRequestBuilder {
        return MockMvcRequestBuilders
            .delete(route)
            .with(SecurityMockMvcRequestPostProcessors.csrf().asHeader())
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
            .with(SecurityMockMvcRequestPostProcessors.csrf().asHeader())
            .contentType("multipart/form-data")
    }

    @Throws(JsonProcessingException::class)
    fun makeRequestBody(`object`: Any): String {
        val mapper = ObjectMapper()
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL)
        return mapper.writeValueAsString(`object`)
    }
}
