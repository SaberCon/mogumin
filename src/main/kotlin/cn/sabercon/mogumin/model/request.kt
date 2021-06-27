package cn.sabercon.mogumin.model

import java.time.LocalDateTime

data class UserParam(
    val username: String,
    val avatar: String,
)

data class ImageParam(
    val name: String,
    val url: String,
    val size: String,
    val id: String? = null,
)

data class FileParam(
    val name: String,
    val url: String,
    val size: String,
    val id: String? = null,
)

data class PasswordParam(
    val name: String,
    val pwd: String,
    val username: String? = null,
    val website: String? = null,
    val desc: String? = null,
    val id: String? = null,
)

data class ReminderParam(
    val summary: String,
    val content: String? = null,
    val link: String? = null,
    val dueDate: LocalDateTime? = null,
    val id: String? = null,
)

data class NoteParam(
    val title: String,
    val content: String,
    val id: String? = null,
)