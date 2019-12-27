package ru.bmstu.testsystem.gateway.model

data class RequestIn (
    var method: String,
    var host: String,
    var port: Int,
    var path: String
)