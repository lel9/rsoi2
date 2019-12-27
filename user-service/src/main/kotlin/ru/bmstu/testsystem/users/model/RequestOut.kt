package ru.bmstu.testsystem.users.model

data class RequestOut (
    var method: String,
    var host: String,
    var port: Int,
    var path: String,
    var id: String
)