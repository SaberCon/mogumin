package cn.sabercon.mogumin.service

import cn.sabercon.mogumin.base.Page
import cn.sabercon.mogumin.base.WebService
import cn.sabercon.mogumin.base.getLoginUserId
import cn.sabercon.mogumin.model.Password
import cn.sabercon.mogumin.model.PasswordParam
import cn.sabercon.mogumin.util.*
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.inValues
import org.springframework.web.bind.annotation.*

@WebService(["pwd"])
class PasswordService(private val mongoOps: ReactiveMongoTemplate) {

    @GetMapping("{id}")
    suspend fun get(@PathVariable id: String): Password {
        return mongoOps.findOne(Password::id eq id, Password::userId eq getLoginUserId())
    }

    @GetMapping
    suspend fun getPage(): Page<Password> {
        return mongoOps.findPage(Password::userId eq getLoginUserId())
    }

    @PostMapping
    suspend fun save(@RequestBody param: PasswordParam) {
        if (param.id == null) {
            mongoOps.insertAndAwait(convertFrom(param, Password::userId to getLoginUserId()))
        } else {
            mongoOps.saveAndAwait(copyFrom(get(param.id), param))
        }
    }

    @DeleteMapping
    suspend fun remove(@RequestBody ids: List<String>) {
        mongoOps.remove<Password>(Password::id inValues ids)
    }
}