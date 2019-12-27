package ru.bmstu.testsystem.result.model

data class RequestOut (
    var method: String,
    var host: String,
    var port: Int,
    var path: String,
    var id: String
)