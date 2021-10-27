package cn.sabercon.megumin.password

import cn.sabercon.common.data.AssetRepository
import cn.sabercon.common.util.EPOCH
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

interface PasswordRepo : AssetRepository<Password>

@Document
data class Password(
    @Id val id: String = "",
    val userId: Long,
    val name: String,
    val pwd: String,
    val username: String?,
    val website: String?,
    val desc: String?,
    val ctime: LocalDateTime = EPOCH,
    val mtime: LocalDateTime = EPOCH,
)