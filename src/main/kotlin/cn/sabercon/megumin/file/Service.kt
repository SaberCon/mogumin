package cn.sabercon.megumin.file

import cn.sabercon.common.data.tx
import cn.sabercon.common.throw400
import cn.sabercon.common.util.copyFromModel
import cn.sabercon.common.util.copyToModel
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class FileHandler(private val repo: FileRepo) {

    suspend fun get(userId: Long, id: String): File? {
        return repo.findByUserIdAndId(userId, id)
    }

    suspend fun list(userId: Long, pageable: Pageable): Flow<File> {
        return repo.findByUserId(userId, pageable)
    }

    suspend fun save(userId: Long, param: FileParam) = tx {
        val image = when {
            param.id.isEmpty() -> param.copyToModel(File::userId to userId)
            else -> get(userId, param.id)?.copyFromModel(param) ?: throw400()
        }
        repo.save(image)
    }

    suspend fun delete(userId: Long, id: String) = tx {
        repo.deleteByUserIdAndId(userId, id)
    }

    suspend fun delete(userId: Long, ids: List<String>) = tx {
        repo.deleteByUserIdAndIdIn(userId, ids)
    }
}