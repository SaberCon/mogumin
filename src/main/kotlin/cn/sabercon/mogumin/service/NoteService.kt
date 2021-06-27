package cn.sabercon.mogumin.service

import cn.sabercon.mogumin.base.Page
import cn.sabercon.mogumin.base.WebService
import cn.sabercon.mogumin.base.getLoginUserId
import cn.sabercon.mogumin.model.Note
import cn.sabercon.mogumin.model.NoteParam
import cn.sabercon.mogumin.util.*
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.inValues
import org.springframework.web.bind.annotation.*

@WebService(["note"])
class NoteService(private val mongoOps: ReactiveMongoTemplate) {

    @GetMapping("{id}")
    suspend fun get(@PathVariable id: String): Note {
        return mongoOps.findOne(Note::id eq id, Note::userId eq getLoginUserId())
    }

    @GetMapping
    suspend fun getPage(): Page<Note> {
        return mongoOps.findPage(Note::userId eq getLoginUserId())
    }

    @PostMapping
    suspend fun save(@RequestBody param: NoteParam) {
        if (param.id == null) {
            mongoOps.insertAndAwait(convertFrom(param, Note::userId to getLoginUserId()))
        } else {
            mongoOps.saveAndAwait(copyFrom(get(param.id), param))
        }
    }

    @DeleteMapping
    suspend fun remove(@RequestBody ids: List<String>) {
        mongoOps.remove<Note>(Note::id inValues ids)
    }
}