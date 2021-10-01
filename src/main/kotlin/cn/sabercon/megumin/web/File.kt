package cn.sabercon.megumin.web

import cn.sabercon.common.WebController
import cn.sabercon.megumin.model.FileParam
import cn.sabercon.megumin.service.FileService
import org.springframework.web.bind.annotation.*

@WebController(["file"])
class FileController(private val service: FileService) {

    @GetMapping("{id}")
    suspend fun get(@PathVariable id: String) = service.get(id)

    @GetMapping
    suspend fun list() = service.list()

    @PostMapping
    suspend fun save(@RequestBody param: FileParam) = service.save(param)

    @DeleteMapping
    suspend fun delete(@RequestBody ids: List<String>) = service.delete(ids)
}