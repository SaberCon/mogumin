package cn.sabercon.megumin.sms

import cn.sabercon.common.ext.*
import cn.sabercon.common.throw400
import cn.sabercon.megumin.user.UserHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.function.server.ServerRequest

@Configuration
class SmsRouterConfig {

    @Bean
    fun smsRouter(handler: SmsHandler, userHandler: UserHandler) = coRouter("/sms") {
        get("/send") {
            val type = it.requestParam<Int>("type")
            val phone = it.getPhone() ?: userHandler.getCurrentUser(it.userId()).phone

            handler.sendCode(type, phone)
            success()
        }

        get("/check") {
            val type = it.requestParam<Int>("type")
            val phone = it.getPhone() ?: userHandler.getCurrentUser(it.userId()).phone
            val code = it.requestParam<String>("code")

            handler.checkCode(type, phone, code)
            success()
        }
    }

    private fun ServerRequest.getPhone(): String? {
        val type = requestParam<Int>("type")
            .let { i -> SmsType.values().firstOrNull { it.code == i } }
            ?: throw400()

        return if (type.authRequired) null else requestParam("phone")
    }
}

enum class SmsType(val code: Int, val authRequired: Boolean) {
    LOGIN(1, false),
    UPDATE_PWD(2, true),
    BIND_PHONE(3, false),
    UNBIND_PHONE(4, true),
}
