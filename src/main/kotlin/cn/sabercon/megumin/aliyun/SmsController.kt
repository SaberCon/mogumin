package cn.sabercon.megumin.aliyun

import cn.sabercon.dgs.codegen.generated.types.DefaultPayload
import cn.sabercon.dgs.codegen.generated.types.SendSmsCodeInput
import cn.sabercon.dgs.codegen.generated.types.SmsCodeValidation
import cn.sabercon.dgs.codegen.generated.types.SmsType
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class SmsController(private val service: SmsService) {

    @MutationMapping
    suspend fun sendSmsCode(@Argument input: SendSmsCodeInput): DefaultPayload {
        service.sendCode(input.type, input.phone)
        return DefaultPayload(input.clientMutationId)
    }

    @QueryMapping
    suspend fun smsCodeValidation(
        @Argument type: SmsType,
        @Argument phone: String,
        @Argument code: String,
    ): SmsCodeValidation {
        val matched = service.checkCode(type, phone, code)
        return SmsCodeValidation(matched)
    }
}
