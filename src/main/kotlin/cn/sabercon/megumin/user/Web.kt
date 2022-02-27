package cn.sabercon.megumin.user

import cn.sabercon.common.ext.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UserRouterConfig {

    @Bean
    fun userRouter(handler: UserHandler) = coRouter("/user") {
        post("/login") {
            val type: LoginType = it.formParam("type")
            val phone: String = it.formParam("phone")
            val code: String = it.formParam("code")
            success(handler.login(type, phone, code))
        }

        get("/current") {
            val user = handler.get(it.userId())
            val currentUser = CurrentUser(user.id, user.username, maskPhoneNumber(user.phone), user.avatar)
            success(currentUser)
        }

        patch("/phone") {
            val phone: String = it.formParam("phone")
            val bindCode: String = it.formParam("bindCode")
            val unbindCode: String = it.formParam("unbindCode")
            handler.updatePhone(it.userId(), phone, bindCode, unbindCode)
            success()
        }

        patch("/pwd") {
            val password: String = it.formParam("password")
            val code: String = it.formParam("code")
            handler.updatePwd(it.userId(), password, code)
            success()
        }

        patch {
            handler.update(it.userId(), it.body())
            success()
        }
    }
}

private fun maskPhoneNumber(phone: String) = phone.replaceRange(3..6, "****")

enum class LoginType { PWD, SMS }

data class UserParam(
    val username: String,
    val avatar: String,
)

data class CurrentUser(
    val id: Long,
    val username: String,
    val phone: String,
    val avatar: String,
)