package cn.sabercon.mogumin.model

import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.mongodb.core.mapping.Document
import java.time.LocalDateTime

@Document
data class User(
    val phone: String,
    val username: String,
    val avatar: String,
    val password: String? = null,
    @Id val id: String? = null,
    @CreatedDate val ctime: LocalDateTime? = null,
    @LastModifiedDate val mtime: LocalDateTime? = null,
)