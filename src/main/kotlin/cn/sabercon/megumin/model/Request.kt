package cn.sabercon.megumin.model

data class UserParam(
    val username: String,
    val avatar: String,
)

data class NoteParam(
    val id: String = "",
    val title: String,
    val content: String,
)

data class ImageParam(
    val id: String = "",
    val name: String,
    val url: String,
    val size: String,
)

data class FileParam(
    val id: String = "",
    val name: String,
    val url: String,
    val size: String,
)

data class PasswordParam(
    val id: String = "",
    val name: String,
    val pwd: String,
    val username: String?,
    val website: String?,
    val desc: String?,
)
