package cn.sabercon.common.data.redis

import cn.sabercon.common.util.parse
import cn.sabercon.common.util.toJson
import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import org.springframework.data.redis.core.getAndAwait
import org.springframework.data.redis.core.setAndAwait
import kotlin.time.Duration
import kotlin.time.toJavaDuration

suspend fun ReactiveStringRedisTemplate.setValue(key: String, value: Any): Boolean {
    return opsForValue().setAndAwait(key, value.toJson())
}

suspend fun ReactiveStringRedisTemplate.setValue(key: String, value: Any, timeout: Duration): Boolean {
    return opsForValue().setAndAwait(key, value.toJson(), timeout.toJavaDuration())
}

suspend inline fun <reified T : Any> ReactiveStringRedisTemplate.getValue(key: String): T? {
    return opsForValue().getAndAwait(key)?.parse()
}