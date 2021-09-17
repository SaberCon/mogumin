package cn.sabercon.megumin.model

data class CurrentUser(
    val id: Long,
    val username: String,
    val phone: String,
    val avatar: String,
)