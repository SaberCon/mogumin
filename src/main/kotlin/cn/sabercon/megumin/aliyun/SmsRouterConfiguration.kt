package cn.sabercon.megumin.aliyun

import cn.sabercon.common.web.body
import cn.sabercon.common.web.empty
import cn.sabercon.common.web.requestParam
import cn.sabercon.common.web.throws
import cn.sabercon.common.web.userId
import cn.sabercon.megumin.user.UserHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus.BAD_REQUEST
import org.springframework.web.reactive.function.server.coRouter

@Configuration
class SmsRouterConfiguration {

    @Bean
    fun smsRouter(userHandler: UserHandler, smsHandler: SmsHandler) = coRouter {

        GET("/sms/send") {
            val type = it.requestParam<Int>("type").toSmsType()
            val phone = if (type.authRequired) userHandler.get(it.userId()).phone else it.requestParam("phone")

            smsHandler.sendCode(type, phone)
            empty()
        }

        GET("/sms/check") {
            val type = it.requestParam<Int>("type").toSmsType()
            val phone = if (type.authRequired) userHandler.get(it.userId()).phone else it.requestParam("phone")
            val code = it.requestParam<String>("code")

            body(smsHandler.checkCode(type, phone, code))
        }
    }

    private fun Int.toSmsType() = SmsType.values().firstOrNull { it.code == this } ?: BAD_REQUEST.throws()
}
