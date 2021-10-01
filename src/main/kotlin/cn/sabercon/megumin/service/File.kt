package cn.sabercon.megumin.service

import cn.sabercon.common.BaseCode
import cn.sabercon.common.data.pageable
import cn.sabercon.common.util.copyFromModel
import cn.sabercon.common.util.copyToModel
import cn.sabercon.common.util.getCurrentUserId
import cn.sabercon.megumin.model.File
import cn.sabercon.megumin.model.FileParam
import cn.sabercon.megumin.repo.FileRepo
import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class FileService(private val repo: FileRepo) {

    suspend fun get(id: String): File? {
        return repo.findByUserIdAndId(getCurrentUserId(), id)
    }

    suspend fun list(): Flow<File> {
        return repo.findByUserId(getCurrentUserId(), pageable())
    }

    @Transactional
    suspend fun save(param: FileParam) {
        val image = when {
            param.id.isEmpty() -> param.copyToModel(File::userId to getCurrentUserId())
            else -> get(param.id)?.copyFromModel(param) ?: BaseCode.BAD_REQUEST.throws()
        }
        repo.save(image)
    }

    @Transactional
    suspend fun delete(ids: List<String>) {
        repo.deleteByUserIdAndIdIn(getCurrentUserId(), ids)
    }
}