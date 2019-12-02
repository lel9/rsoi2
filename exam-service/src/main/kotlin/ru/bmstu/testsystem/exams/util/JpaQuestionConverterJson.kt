package ru.bmstu.testsystem.exams.util

import ru.bmstu.testsystem.exams.domain.Question
import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import java.io.IOException
import javax.persistence.AttributeConverter
import javax.persistence.Converter


@Converter(autoApply = true)
class JpaQuestionConverterJson : AttributeConverter<List<Question>, String> {

    override fun convertToDatabaseColumn(meta: List<Question>): String? {
        try {
            return objectMapper.writeValueAsString(meta)
        } catch (ex: JsonProcessingException) {
            return null
        }

    }

    override fun convertToEntityAttribute(dbData: String): List<Question>? {
        try {
            return objectMapper.readValue(dbData, object : TypeReference<List<Question>>() {})
        } catch (ex: IOException) {
            return null
        }

    }

    private val objectMapper = ObjectMapper()

}
