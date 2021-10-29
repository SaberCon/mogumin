package cn.sabercon.megumin.note

import cn.sabercon.common.data.r2dbc.tx
import cn.sabercon.common.throw400
import cn.sabercon.common.util.copyFromModel
import cn.sabercon.common.util.copyToModel
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class NoteHandler(private val repo: NoteRepo) {

    suspend fun get(userId: Long, id: String): Note? {
        return repo.findByUserIdAndId(userId, id)
    }

    suspend fun list(userId: Long, pageable: Pageable): Flow<Note> {
        return repo.findByUserId(userId, pageable)
    }

    suspend fun save(userId: Long, param: NoteParam) = tx {
        val image = when {
            param.id.isEmpty() -> param.copyToModel(Note::userId to userId)
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