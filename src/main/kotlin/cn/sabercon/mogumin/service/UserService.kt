package cn.sabercon.mogumin.service

import cn.sabercon.mogumin.model.User
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("user")
class UserService(private val mongoOps: ReactiveMongoTemplate, private val redisOps: ReactiveStringRedisTemplate) {

    @PostMapping
    suspend fun save(user: User): User {
        return mongoOps.save(user).awaitSingle()
    }
}