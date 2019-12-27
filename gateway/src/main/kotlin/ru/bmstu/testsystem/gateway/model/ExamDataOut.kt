package ru.bmstu.testsystem.gateway.model


data class ExamDataOut (
    var id: String,
    var name: String,
    var passes: Int,
    var description: String,
    var questionIns: List<QuestionDataOut>
){
    constructor() : this("", "", 0, "", arrayListOf())
}
