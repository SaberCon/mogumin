package cn.sabercon.megumin.service

import cn.sabercon.common.BaseCode
import cn.sabercon.common.data.pageable
import cn.sabercon.common.util.copyFromModel
import cn.sabercon.common.util.copyToModel
import cn.sabercon.common.util.getCurrentUserId
import cn.sabercon.megumin.model.Image
import cn.sabercon.megumin.model.ImageParam
import cn.sabercon.megumin.repo.ImageRepo
import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ImageService(private val repo: ImageRepo) {

    suspend fun get(id: String): Image? {
        return repo.findByUserIdAndId(getCurrentUserId(), id)
    }

    suspend fun list(): Flow<Image> {
        return repo.findByUserId(getCurrentUserId(), pageable())
    }

    @Transactional
    suspend fun save(param: ImageParam) {
        val image = when {
            param.id.isEmpty() -> param.copyToModel(Image::userId to getCurrentUserId())
            else -> get(param.id)?.copyFromModel(param) ?: BaseCode.BAD_REQUEST.throws()
        }
        repo.save(image)
    }

    @Transactional
    suspend fun delete(ids: List<String>) {
        repo.deleteByUserIdAndIdIn(getCurrentUserId(), ids)
    }
}