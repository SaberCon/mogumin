package cn.sabercon.megumin.model

import cn.sabercon.common.util.EPOCH
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

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