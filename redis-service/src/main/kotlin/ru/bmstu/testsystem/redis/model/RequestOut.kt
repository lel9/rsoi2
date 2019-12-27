package ru.bmstu.testsystem.redis.model

import ru.bmstu.testsystem.redis.domian.Request

data class RequestOut (
    var method: String,
    var host: String,
    var port: Int,
    var path: String,
    var id: String
) {
    constructor(req: Request): this(req.method, req.host, req.port, req.path, req.id)
}