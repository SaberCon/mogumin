package cn.sabercon.megumin.user

import cn.sabercon.common.Jwt
import cn.sabercon.common.util.sha256
import cn.sabercon.dgs.codegen.generated.types.LoginType
import cn.sabercon.dgs.codegen.generated.types.SmsType
import cn.sabercon.dgs.codegen.generated.types.UpdateUserInput
import cn.sabercon.megumin.aliyun.SmsService
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class UserService(private val smsService: SmsService, private val repo: UserRepo, private val jwt: Jwt) {

    suspend fun login(type: LoginType, phone: String, code: String): String {
        val user = when (type) {
            LoginType.PWD -> {
                repo.findByPhone(phone)?.takeIf { it.password == sha256(code) }
                    ?: throw IllegalArgumentException("Wrong phone or password")
            }
            LoginType.SMS -> {
                require(smsService.checkCode(SmsType.LOGIN, phone, code))
                repo.findByPhone(phone) ?: register(phone)
            }
        }
        return jwt.createToken(user.id)
    }

    private suspend fun register(phone: String): User {
        val user = User(
            username = generateUsername(),
            password = "",
            phone = phone,
            avatar = "http://oss.sabercon.cn/base/takagi.jpg",
        )
        return repo.save(user)
    }

    suspend fun updatePhone(userId: Long, phone: String, bindCode: String, unbindCode: String) {
        val user = get(userId)
        require(smsService.checkCode(SmsType.BIND_PHONE, phone, bindCode))
        require(smsService.checkCode(SmsType.UNBIND_PHONE, user.phone, unbindCode))
        require(!repo.existsByPhone(phone))
        repo.save(user.copy(phone = phone))
    }

    suspend fun updatePassword(userId: Long, password: String, code: String) {
        val user = get(userId)
        require(smsService.checkCode(SmsType.UPDATE_PASSWORD, user.phone, code))
        repo.save(user.copy(password = sha256(password)))
    }

    suspend fun update(userId: Long, input: UpdateUserInput): User {
        return get(userId).run {
            copy(
                username = input.username ?: username,
                avatar = input.avatar ?: avatar,
            )
        }.let { repo.save(it) }
    }

    suspend fun get(userId: Long): User {
        return repo.findById(userId)!!
    }

    private fun generateUsername(): String = "user${Random.nextInt(100_000_000).toString().padStart(8, '0')}"
}
