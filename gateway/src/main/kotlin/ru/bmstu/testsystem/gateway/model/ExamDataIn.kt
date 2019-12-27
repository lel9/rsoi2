package ru.bmstu.testsystem.gateway.model
import io.swagger.annotations.*
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

@ApiModel(value = "ExamDataIn")
data class ExamDataIn (
        @field:NotNull(message="Название теста должен быть задано")
        @field:NotEmpty(message="Название теста не может быть пусто")
        var name: String?,

        @field:NotNull(message="Описание должен быть задано")
        @field:NotEmpty(message="Описание не может быть пусто")
        var description: String?,

        @field:NotNull(message="Список вопросов должен быть задан")
        @field:NotEmpty(message="Список вопросов не может быть пуст")
        var questionIns: MutableList<QuestionDataIn>?
)
