package cn.sabercon.mogumin.service

import cn.sabercon.mogumin.model.User
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import org.springframework.stereotype.Service

@Service
class UserService(private val mongoOps: ReactiveMongoTemplate, private val redisOps: ReactiveStringRedisTemplate) {

    suspend fun save(user: User): User {
        return mongoOps.save(user).awaitSingle()
    }
}