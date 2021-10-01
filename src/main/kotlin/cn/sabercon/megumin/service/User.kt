package cn.sabercon.megumin.service

import cn.sabercon.common.BaseCode
import cn.sabercon.common.util.JwtUtils
import cn.sabercon.common.util.ensure
import cn.sabercon.common.util.getCurrentUserId
import cn.sabercon.common.util.sha256
import cn.sabercon.megumin.model.User
import cn.sabercon.megumin.model.UserParam
import cn.sabercon.megumin.repo.UserRepo
import cn.sabercon.megumin.web.SmsType
import org.springframework.stereotype.Service
import kotlin.random.Random

@Service
class UserService(private val smsService: SmsService, private val repo: UserRepo) {

    suspend fun login(type: LoginType, phone: String, code: String): String {
        return when (type) {
            LoginType.PWD -> repo.findByPhone(phone)?.takeIf { it.password == sha256(code) }
                ?: BaseCode.LOGIN_ERROR.throws()
            LoginType.SMS -> {
                smsService.checkCode(SmsType.LOGIN.code, phone, code)
                repo.findByPhone(phone) ?: register(phone)
            }
        }.let { JwtUtils.createToken(it.id) }
    }

    private suspend fun register(phone: String): User {
        return repo.save(User(phone = phone, avatar = DEFAULT_AVATAR, username = generateUsername()))
    }

    private fun generateUsername() = "user${Random.nextInt(100_000_000).toString().padStart(8, '0')}"

    suspend fun updatePhone(phone: String, unbindCode: String, bindCode: String) {
        val user = getCurrentUser()
        smsService.checkCode(SmsType.BIND_PHONE.code, phone, bindCode)
        smsService.checkCode(SmsType.UNBIND_PHONE.code, user.phone, unbindCode)
        ensure(!repo.existsByPhone(phone), BaseCode.PHONE_BOUND)
        repo.save(user.copy(phone = phone))
    }

    suspend fun updatePwd(password: String, code: String) {
        val user = getCurrentUser()
        smsService.checkCode(SmsType.UPDATE_PWD.code, user.phone, code)
        repo.save(user.copy(password = sha256(password)))
    }

    suspend fun update(param: UserParam) {
        repo.save(getCurrentUser().copy(username = param.username, avatar = param.avatar))
    }

    suspend fun getCurrentUser() = repo.findById(getCurrentUserId()) ?: BaseCode.UNAUTHORIZED.throws()
}

enum class LoginType { PWD, SMS }

const val DEFAULT_AVATAR = "http://oss.sabercon.cn/base/takagi.jpg"