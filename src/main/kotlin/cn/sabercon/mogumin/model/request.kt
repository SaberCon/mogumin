package cn.sabercon.mogumin.model

data class LoginParam(
    val phone: String,
    // the password or sms code
    val code: String,
    val type: LoginType,
)

enum class LoginType { PWD, SMS }

data class UserUpdater(
    val username: String,
    val avatar: String,
)