package ru.bmstu.testsystem.redis.domian

import org.springframework.data.redis.core.RedisHash
import org.springframework.data.redis.core.index.Indexed
import ru.bmstu.testsystem.redis.model.RequestIn
import java.util.*

@RedisHash("Request")
data class Request (
    var method: String,
    @field:Indexed
    var host: String,
    @field:Indexed
    var port: Int,
    var path: String
) {
    var id: String = UUID.randomUUID().toString()
    constructor(req: RequestIn): this(req.method, req.host, req.port, req.path)
}