package cn.sabercon.megumin.user

import cn.sabercon.common.graphql.DefaultPayload
import cn.sabercon.dgs.codegen.generated.types.LoginInput
import cn.sabercon.dgs.codegen.generated.types.LoginPayload
import cn.sabercon.dgs.codegen.generated.types.UpdateUserInput
import cn.sabercon.dgs.codegen.generated.types.UpdateUserPasswordInput
import cn.sabercon.dgs.codegen.generated.types.UpdateUserPhoneInput
import org.springframework.graphql.data.method.annotation.Argument
import org.springframework.graphql.data.method.annotation.ContextValue
import org.springframework.graphql.data.method.annotation.MutationMapping
import org.springframework.graphql.data.method.annotation.QueryMapping
import org.springframework.stereotype.Controller

@Controller
class UserController(private val service: UserService) {

    @MutationMapping
    suspend fun login(@Argument input: LoginInput): LoginPayload {
        val token = service.login(input.type, input.phone, input.code)
        return LoginPayload(input.clientMutationId, token)
    }

    @MutationMapping
    suspend fun updateUserPhone(@ContextValue userId: Long, @Argument input: UpdateUserPhoneInput): DefaultPayload {
        service.updatePhone(userId, input.phone, input.bindCode, input.unbindCode)
        return DefaultPayload(input.clientMutationId)
    }

    @MutationMapping
    suspend fun updateUserPassword(
        @ContextValue userId: Long,
        @Argument input: UpdateUserPasswordInput,
    ): DefaultPayload {
        service.updatePassword(userId, input.password, input.code)
        return DefaultPayload(input.clientMutationId)
    }

    @MutationMapping
    suspend fun updateUser(@ContextValue userId: Long, @Argument input: UpdateUserInput): DefaultPayload {
        service.update(userId, input)
        return DefaultPayload(input.clientMutationId)
    }

    @QueryMapping
    suspend fun viewer(@ContextValue userId: Long): User {
        return service.get(userId)
    }
}
