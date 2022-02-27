package cn.sabercon.common.util

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.ZoneOffset
import java.util.*

@Component
class Jwt(@Value("\${sabercon.jwt-key}") key: String, @Value("\${sabercon.jwt-days:30}") expiration: Int) {

    private val algorithm = Algorithm.HMAC256(key)

    private val verifier = JWT.require(algorithm).build()

    private val expiration = expiration.days

    fun createToken(userId: Long): String = JWT.create()
        .withSubject(userId.toString())
        .withExpiresAt(Date.from((now() + expiration).toInstant(ZoneOffset.UTC)))
        .sign(algorithm)!!

    fun decodeToken(token: String): Long? = runCatching { verifier.verify(token) }
        .map { it.subject.toLong() }
        .getOrNull()
}