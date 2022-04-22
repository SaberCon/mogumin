package cn.sabercon.megumin.user

import cn.sabercon.common.Jwt
import cn.sabercon.common.util.sha256
import cn.sabercon.common.web.throws
import cn.sabercon.megumin.aliyun.SmsHandler
import cn.sabercon.megumin.aliyun.SmsType
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class UserHandler(private val smsHandler: SmsHandler, private val repo: UserRepo, private val jwt: Jwt) {

    suspend fun login(type: LoginType, phone: String, code: String): String {
        val user = when (type) {
            LoginType.PWD -> repo.findByPhone(phone)?.takeIf { it.password == sha256(code) }
                ?: HttpStatus.BAD_REQUEST.throws()
            LoginType.SMS -> {
                smsHandler.checkCode(SmsType.LOGIN, phone, code)
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
            avatar = DEFAULT_AVATAR,
        )
        return repo.save(user)
    }

    suspend fun updatePhone(userId: Long, phone: String, bindCode: String, unbindCode: String) {
        val user = get(userId)
        smsHandler.checkCode(SmsType.BIND_PHONE, phone, bindCode)
        smsHandler.checkCode(SmsType.UNBIND_PHONE, user.phone, unbindCode)
        if (repo.existsByPhone(phone)) {
            HttpStatus.BAD_REQUEST.throws()
        }
        repo.save(user.copy(phone = phone))
    }

    suspend fun updatePwd(userId: Long, password: String, code: String) {
        val user = get(userId)
        smsHandler.checkCode(SmsType.UPDATE_PWD, user.phone, code)
        repo.save(user.copy(password = sha256(password)))
    }

    suspend fun update(userId: Long, param: UserParam) {
        val user = get(userId).copy(
            username = param.username,
            avatar = param.avatar,
        )
        repo.save(user)
    }

    suspend fun get(userId: Long): User {
        return repo.findById(userId)!!
    }

    private fun generateUsername(): String = "user${Random.nextInt(100_000_000).toString().padStart(8, '0')}"
}

private const val DEFAULT_AVATAR = "http://oss.sabercon.cn/base/takagi.jpg"
