package cn.sabercon.mogumin.service

import cn.sabercon.mogumin.base.BaseCode
import cn.sabercon.mogumin.base.WebService
import cn.sabercon.mogumin.extension.*
import cn.sabercon.mogumin.model.User
import cn.sabercon.mogumin.model.CurrentUser
import cn.sabercon.mogumin.model.UserParam
import cn.sabercon.mogumin.util.*
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import kotlin.random.Random

@WebService(["user"])
class UserService(
    private val mongoOps: ReactiveMongoTemplate,
    private val smsService: SmsService,
) {

    @PostMapping("login")
    suspend fun login(type: LoginType, phone: String, code: String): String {
        val user: User = when (type) {
            LoginType.PWD -> {
                mongoOps.findOneOrNull(User::phone eq phone, User::password eq sha256(code))
                    ?: BaseCode.LOGIN_ERROR.throws()
            }
            LoginType.SMS -> {
                assertTrue(smsService.checkCode(SmsType.LOGIN.code, phone, code), BaseCode.SMS_CODE_WRONG)
                mongoOps.findOneOrNull(User::phone eq phone) ?: register(phone)
            }
        }
        return JwtUtils.createToken(user.id!!)
    }

    private suspend fun register(phone: String): User {
        return mongoOps.insertAndAwait(User(phone = phone, avatar = DEFAULT_AVATAR, username = generateUsername()))
    }

    private fun generateUsername(): String {
        return "user${Random.nextInt(100_000_000).toString().padStart(8, '0')}"
    }

    @GetMapping("current")
    suspend fun getCurrentUser(): CurrentUser {
        val user = findCurrentUser()
        return convertFrom(findCurrentUser(), CurrentUser::phone to maskPhoneNumber(user.phone))
    }

    private fun maskPhoneNumber(phone: String): String {
        return phone.replaceRange(3..6, "****")
    }

    @PutMapping("phone")
    suspend fun updatePhone(phone: String, unbindCode: String, bindCode: String) {
        val user = findCurrentUser()
        assertTrue(smsService.checkCode(SmsType.BIND_PHONE.code, phone, bindCode), BaseCode.SMS_CODE_WRONG)
        assertTrue(smsService.checkCode(SmsType.UNBIND_PHONE.code, user.phone, unbindCode), BaseCode.SMS_CODE_WRONG)
        assertTrue(!mongoOps.exists<User>(User::phone eq phone), BaseCode.PHONE_ALREADY_BOUND)
        mongoOps.saveAndAwait(user.copy(phone = phone))
    }

    @PutMapping("pwd")
    suspend fun updatePwd(password: String, code: String) {
        val user = findCurrentUser()
        assertTrue(smsService.checkCode(SmsType.UPDATE_PWD.code, user.phone, code), BaseCode.SMS_CODE_WRONG)
        mongoOps.saveAndAwait(user.copy(password = sha256(password)))
    }

    @PutMapping
    suspend fun update(@RequestBody param: UserParam) {
        mongoOps.saveAndAwait(findCurrentUser().copy(username = param.username, avatar = param.avatar))
    }

    suspend fun findCurrentUser() = mongoOps.find<User>(getCurrentUserId())
}

enum class LoginType { PWD, SMS }

const val DEFAULT_AVATAR = "http://oss.sabercon.cn/base/takagi.jpg"