package ru.bmstu.testsystem.result.model

data class AppCredential (
    var appId: String,
    var appSecret: String
)

fun resolve(host: String, port: Int): AppCredential? {
    if (host == "localhost" && port == 8081)
        return AppCredential("userService", "vbwEAD63e0NPL1IbfzaXfg==")
    if (host == "localhost" && port == 8082)
        return AppCredential("examService", "o0/ienSpZ4wP8ttq+BN/JQ==")
    if (host == "localhost" && port == 8083)
        return AppCredential("resultService", "Jys/QbRm1vGI0sOdXp0UbQ==")
    return null
}