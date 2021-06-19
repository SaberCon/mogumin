package cn.sabercon.mogumin.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
data class User(
    val username: String,
    val password: String,
    val phone: String,
    val avatar: String,
    @Id val id: String? = null,
    @CreatedDate val ctime: LocalDateTime? = null,
    @LastModifiedDate val mtime: LocalDateTime? = null,
)