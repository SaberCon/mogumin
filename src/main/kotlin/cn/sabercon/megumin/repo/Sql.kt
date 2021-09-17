package cn.sabercon.megumin.repo

import cn.sabercon.megumin.model.User
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface UserRepo : CoroutineCrudRepository<User, Long> {

    suspend fun findByPhone(phone: String): User?

    suspend fun existsByPhone(phone: String): Boolean
}

