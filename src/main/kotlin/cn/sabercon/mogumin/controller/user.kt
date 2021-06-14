package cn.sabercon.mogumin.controller

import cn.sabercon.mogumin.model.User
import cn.sabercon.mogumin.service.UserService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("user")
class UserController(private val service: UserService) {

    @PostMapping
    suspend fun save(user: User): User = service.save(user)
}