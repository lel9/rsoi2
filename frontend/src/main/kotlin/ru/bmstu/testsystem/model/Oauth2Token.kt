package ru.bmstu.testsystem.model

data class Oauth2Token (
        var access_token: String,
        var token_type: String,
        var refresh_token: String,
        var expires_in: Long,
        var scope: String
)

data class Oauth2TokenSimple (
        var access_token: String,
        var refresh_token: String
)