package cn.sabercon.megumin.ctrl

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
    suspend fun login(type: LoginType, phone: String, code: String) = service.login(type, phone, code)

    @GetMapping("current")
    suspend fun getCurrentUser() = service.getCurrentUser().let {
        it.copyToModel(CurrentUser::phone to maskPhoneNumber(it.phone))
    }

    @PutMapping("phone")
    suspend fun updatePhone(phone: String, unbindCode: String, bindCode: String) =
        service.updatePhone(phone, unbindCode, bindCode)

    @PutMapping("pwd")
    suspend fun updatePwd(password: String, code: String) = service.updatePwd(password, code)

    @PutMapping
    suspend fun update(@RequestBody param: UserParam) = service.update(param)

    private fun maskPhoneNumber(phone: String) = phone.replaceRange(3..6, "****")
}