package cn.sabercon.mogumin.service

import cn.sabercon.mogumin.base.Page
import cn.sabercon.mogumin.base.WebService
import cn.sabercon.mogumin.extension.*
import cn.sabercon.mogumin.model.Reminder
import cn.sabercon.mogumin.model.ReminderParam
import cn.sabercon.mogumin.util.convertFrom
import cn.sabercon.mogumin.util.copyFrom
import cn.sabercon.mogumin.util.getLoginUserId
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.inValues
import org.springframework.web.bind.annotation.*

@WebService(["reminder"])
class ReminderService(private val mongoOps: ReactiveMongoTemplate) {

    @GetMapping("{id}")
    suspend fun get(@PathVariable id: String): Reminder {
        return mongoOps.findOne(Reminder::id eq id, Reminder::userId eq getLoginUserId())
    }

    @GetMapping
    suspend fun getPage(): Page<Reminder> {
        return mongoOps.findPage(Reminder::userId eq getLoginUserId())
    }

    @PostMapping
    suspend fun save(@RequestBody param: ReminderParam) {
        if (param.id == null) {
            mongoOps.insertAndAwait(convertFrom(param, Reminder::userId to getLoginUserId()))
        } else {
            mongoOps.saveAndAwait(copyFrom(get(param.id), param))
        }
    }

    @DeleteMapping
    suspend fun remove(@RequestBody ids: List<String>) {
        mongoOps.remove<Reminder>(Reminder::id inValues ids)
    }
}