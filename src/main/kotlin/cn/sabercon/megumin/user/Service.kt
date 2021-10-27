package cn.sabercon.megumin.user

import cn.sabercon.common.throw500
import cn.sabercon.common.util.JwtUtils
import cn.sabercon.common.util.ensure
import cn.sabercon.common.util.sha256
import cn.sabercon.megumin.MeguminCode
import cn.sabercon.megumin.sms.SmsHandler
import cn.sabercon.megumin.sms.SmsType
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class UserHandler(private val smsHandler: SmsHandler, private val repo: UserRepo) {

    suspend fun login(type: LoginType, phone: String, code: String): String {
        val user = when (type) {
            LoginType.PWD -> repo.findByPhone(phone)?.takeIf { it.password == sha256(code) }
                ?: MeguminCode.LOGIN_ERROR.throws()
            LoginType.SMS -> {
                smsHandler.checkCode(SmsType.LOGIN.code, phone, code)
                repo.findByPhone(phone) ?: register(phone)
            }
        }
        return JwtUtils.createToken(user.id)
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
        val user = getCurrentUser(userId)
        smsHandler.checkCode(SmsType.BIND_PHONE.code, phone, bindCode)
        smsHandler.checkCode(SmsType.UNBIND_PHONE.code, user.phone, unbindCode)
        ensure(!repo.existsByPhone(phone), MeguminCode.PHONE_BOUND)
        repo.save(user.copy(phone = phone))
    }

    suspend fun updatePwd(userId: Long, password: String, code: String) {
        val user = getCurrentUser(userId)
        smsHandler.checkCode(SmsType.UPDATE_PWD.code, user.phone, code)
        repo.save(user.copy(password = sha256(password)))
    }

    suspend fun update(userId: Long, param: UserParam) {
        val user = getCurrentUser(userId).copy(
            username = param.username,
            avatar = param.avatar,
        )
        repo.save(user)
    }

    suspend fun getCurrentUser(userId: Long) = repo.findById(userId) ?: throw500()
}

private fun generateUsername() = "user${Random.nextInt(100_000_000).toString().padStart(8, '0')}"

private const val DEFAULT_AVATAR = "http://oss.sabercon.cn/base/takagi.jpg"