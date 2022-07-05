package cn.sabercon.common

import cn.sabercon.common.util.now
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import java.time.ZoneOffset
import java.util.*
import kotlin.time.Duration.Companion.days
import kotlin.time.toJavaDuration

@Component
class Jwt(@Value("\${sabercon.secret}") key: String) {

    private val algorithm = Algorithm.HMAC256(key)

    private val verifier = JWT.require(algorithm).build()

    private val expiration = 30.days.toJavaDuration()

    fun createToken(userId: Long): String {
        return JWT.create()
            .withSubject(userId.toString())
            .withExpiresAt(Date.from((now() + expiration).toInstant(ZoneOffset.UTC)))
            .sign(algorithm)
    }

    /**
     * @return `null` if [token] is invalid
     */
    fun decodeToken(token: String): Long? {
        return runCatching { verifier.verify(token) }
            .map { it.subject.toLong() }
            .getOrNull()
    }

    /**
     * @return `0` if `Authorization` header is absent or invalid
     */
    fun decodeAuthorizationHeader(headers: HttpHeaders): Long {
        return headers[HttpHeaders.AUTHORIZATION]
            ?.getOrNull(0)
            ?.takeIf { it.startsWith("Bearer ", true) }
            ?.substring("Bearer ".length)
            ?.let { decodeToken(it) }
            ?: 0
    }
}
