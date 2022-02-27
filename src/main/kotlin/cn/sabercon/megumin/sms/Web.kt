package cn.sabercon.megumin.sms

import cn.sabercon.common.ext.*
import cn.sabercon.common.throwClientError
import cn.sabercon.megumin.user.UserHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SmsRouterConfig {

    @Bean
    fun smsRouter(handler: SmsHandler, userHandler: UserHandler) = coRouter("/sms") {
        get("/send") {
            val type: SmsType = it.requestParam<Int>("type").toSmsType()
            val phone: String = if (type.authRequired) userHandler.get(it.userId()).phone else it.requestParam("phone")

            handler.sendCode(type, phone)
            success()
        }

        get("/check") {
            val type: SmsType = it.requestParam<Int>("type").toSmsType()
            val phone: String = if (type.authRequired) userHandler.get(it.userId()).phone else it.requestParam("phone")
            val code: String = it.requestParam("code")

            handler.checkCode(type, phone, code)
            success()
        }
    }

    private fun Int.toSmsType(): SmsType =
        SmsType.values().firstOrNull { it.code == this } ?: throwClientError("Wrong sms type")
}

enum class SmsType(val code: Int, val authRequired: Boolean) {
    LOGIN(1, false),
    UPDATE_PWD(2, true),
    BIND_PHONE(3, false),
    UNBIND_PHONE(4, true),
}
