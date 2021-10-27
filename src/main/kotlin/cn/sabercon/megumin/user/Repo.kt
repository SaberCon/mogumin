package cn.sabercon.megumin.user

import cn.sabercon.common.util.EPOCH
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import org.springframework.data.repository.kotlin.CoroutineCrudRepository
import java.time.LocalDateTime

interface UserRepo : CoroutineCrudRepository<User, Long> {

    suspend fun findByPhone(phone: String): User?

    suspend fun existsByPhone(phone: String): Boolean
}

@Table
data class User(
    @Id val id: Long = 0,
    val username: String,
    val password: String,
    val phone: String,
    val avatar: String,
    val ctime: LocalDateTime = EPOCH,
    val mtime: LocalDateTime = EPOCH,
)