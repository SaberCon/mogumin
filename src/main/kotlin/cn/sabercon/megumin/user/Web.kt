package cn.sabercon.megumin.user

import cn.sabercon.common.ext.*
import cn.sabercon.common.util.copyToModel
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class UserRouterConfig {

    @Bean
    fun userRouter(handler: UserHandler) = coRouter("/user") {
        post("/login") {
            val type = it.formParam<LoginType>("type")
            val phone = it.formParam<String>("phone")
            val code = it.formParam<String>("code")
            success(handler.login(type, phone, code))
        }

        get("/current") {
            val user = handler.getCurrentUser(it.userId())
            success(user.copyToModel(CurrentUser::phone to maskPhoneNumber(user.phone)))
        }

        put("/phone") {
            val phone = it.formParam<String>("phone")
            val bindCode = it.formParam<String>("bindCode")
            val unbindCode = it.formParam<String>("unbindCode")
            handler.updatePhone(it.userId(), phone, bindCode, unbindCode)
            success()
        }

        put("/pwd") {
            val password = it.formParam<String>("password")
            val code = it.formParam<String>("code")
            handler.updatePwd(it.userId(), password, code)
            success()
        }

        put {
            handler.update(it.userId(), it.validatedBody())
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