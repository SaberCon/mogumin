package cn.sabercon.megumin.ctrl

import cn.sabercon.common.WebController
import cn.sabercon.megumin.model.NoteParam
import cn.sabercon.megumin.service.NoteService
import org.springframework.web.bind.annotation.*

@WebController(["note"])
class NoteController(private val service: NoteService) {

    @GetMapping("{id}")
    suspend fun get(@PathVariable id: String) = service.get(id)

    @GetMapping
    suspend fun list() = service.list()

    @PostMapping
    suspend fun save(@RequestBody param: NoteParam) = service.save(param)

    @DeleteMapping
    suspend fun delete(@RequestBody ids: List<String>) = service.delete(ids)
}