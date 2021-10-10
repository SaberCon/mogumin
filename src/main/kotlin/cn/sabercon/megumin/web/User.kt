package cn.sabercon.megumin.web

import cn.sabercon.common.WebController
import cn.sabercon.common.util.copyToModel
import cn.sabercon.megumin.model.CurrentUser
import cn.sabercon.megumin.model.UserParam
import cn.sabercon.megumin.service.LoginType
import cn.sabercon.megumin.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody

@WebController(["user"])
class UserController(private val service: UserService) {

    @PostMapping("login")
    suspend fun login(param: LoginParam) = service.login(param.type, param.phone, param.code)

    @GetMapping("current")
    suspend fun getCurrentUser() = service.getCurrentUser().let {
        it.copyToModel(CurrentUser::phone to maskPhoneNumber(it.phone))
    }

    @PutMapping("phone")
    suspend fun updatePhone(param: UpdatePhoneParam) =
        service.updatePhone(param.phone, param.unbindCode, param.bindCode)

    @PutMapping("pwd")
    suspend fun updatePwd(param: UpdatePwdParam) = service.updatePwd(param.password, param.code)

    @PutMapping
    suspend fun update(@RequestBody param: UserParam) = service.update(param)
}

private fun maskPhoneNumber(phone: String) = phone.replaceRange(3..6, "****")

data class LoginParam(
    val type: LoginType,
    val phone: String,
    val code: String,
)

data class UpdatePhoneParam(
    val phone: String,
    val unbindCode: String,
    val bindCode: String,
)

data class UpdatePwdParam(
    val password: String,
    val code: String,
)