package cn.sabercon.megumin.web

import cn.sabercon.common.WebController
import cn.sabercon.megumin.model.PasswordParam
import cn.sabercon.megumin.service.PasswordService
import org.springframework.web.bind.annotation.*

@WebController(["pwd"])
class PasswordController(private val service: PasswordService) {

    @GetMapping("{id}")
    suspend fun get(@PathVariable id: String) = service.get(id)

    @GetMapping
    suspend fun list() = service.list()

    @PostMapping
    suspend fun save(@RequestBody param: PasswordParam) = service.save(param)

    @DeleteMapping
    suspend fun delete(@RequestBody ids: List<String>) = service.delete(ids)
}