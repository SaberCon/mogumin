package cn.sabercon.common.util

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import java.time.ZoneOffset
import java.util.*

private val EXPIRATION = 30.days

object JwtUtils {
    private val secretKey by lazy { ContextHolder.getProperty("sabercon.jwt-key") }

    private val algorithm by lazy { Algorithm.HMAC256(secretKey) }

    private val verifier by lazy { JWT.require(algorithm).build() }

    fun createToken(userId: Long) = JWT.create()
        .withSubject(userId.toString())
        .withExpiresAt(Date.from((now + EXPIRATION).toInstant(ZoneOffset.UTC)))
        .sign(algorithm)!!

    fun decodeToken(token: String) = runCatching { verifier.verify(token) }
        .map { it.subject.toLong() }
        .onFailure { log.debug("Error when decoding token: {}", it.message) }
        .getOrNull()
}