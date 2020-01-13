package ru.bmstu.testsystem.result.web

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import ru.bmstu.testsystem.result.model.AppCredential
import ru.bmstu.testsystem.result.model.resolve
import java.util.*
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

@RestController
class TokenController {

    @Value("\${server.address}")
    private lateinit var host: String

    @Value("\${server.port}")
    private var port: Int? = null

    @Autowired
    private lateinit var cipher: Cipher

    @PostMapping("/api/v1/token")
    fun getToken(@RequestBody credential: AppCredential): ResponseEntity<String> {
        val myCredential = resolve(host, port!!)!!
        if (myCredential.appId == credential.appId && myCredential.appSecret == credential.appSecret) {
            cipher.init(Cipher.ENCRYPT_MODE, SecretKeySpec(Base64.getDecoder().decode(myCredential.appSecret.toByteArray()), "AES"));
            val plainText = "${System.currentTimeMillis() + 30000}:${UUID.randomUUID()}".toByteArray(charset("UTF-8"))
            val cipherText = Base64.getEncoder().encodeToString(cipher.doFinal(plainText))
            return ResponseEntity.ok(cipherText)
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
    }
}