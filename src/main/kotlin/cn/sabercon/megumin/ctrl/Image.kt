package cn.sabercon.megumin.ctrl

import cn.sabercon.common.WebController
import cn.sabercon.megumin.model.ImageParam
import cn.sabercon.megumin.service.ImageService
import org.springframework.web.bind.annotation.*

@WebController(["img"])
class ImageController(private val service: ImageService) {

    @GetMapping("{id}")
    suspend fun get(@PathVariable id: String) = service.get(id)

    @GetMapping
    suspend fun list() = service.list()

    @PostMapping
    suspend fun save(@RequestBody param: ImageParam) = service.save(param)

    @DeleteMapping
    suspend fun delete(@RequestBody ids: List<String>) = service.delete(ids)
}