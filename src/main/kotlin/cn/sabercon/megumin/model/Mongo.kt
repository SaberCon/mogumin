package cn.sabercon.megumin.model

import cn.sabercon.common.util.EPOCH
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
data class Note(
    @Id val id: String = "",
    val userId: Long,
    val title: String,
    val content: String,
    val ctime: LocalDateTime = EPOCH,
    val mtime: LocalDateTime = EPOCH,
)

@Document
data class Image(
    @Id val id: String = "",
    val userId: Long,
    val name: String,
    val url: String,
    val size: String,
    val ctime: LocalDateTime = EPOCH,
    val mtime: LocalDateTime = EPOCH,
)

@Document
data class File(
    @Id val id: String = "",
    val userId: Long,
    val name: String,
    val url: String,
    val size: String,
    val ctime: LocalDateTime = EPOCH,
    val mtime: LocalDateTime = EPOCH,
)

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
