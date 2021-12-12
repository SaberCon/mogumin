package cn.sabercon.megumin.note

import cn.sabercon.common.data.DEFAULT_SORT
import cn.sabercon.common.data.r2dbc.tx
import cn.sabercon.common.throw400
import cn.sabercon.common.util.convertData
import cn.sabercon.common.util.mergeData
import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Service

@Service
class NoteHandler(private val repo: NoteRepo) {

    suspend fun get(userId: Long, id: String): Note? {
        return repo.findByUserIdAndId(userId, id)
    }

    fun list(userId: Long): Flow<Note> {
        return repo.findByUserId(userId, DEFAULT_SORT)
    }

    suspend fun save(userId: Long, param: NoteParam) = tx {
        val note = when (val id = param.id) {
            "" -> param.convertData(Note::userId to userId)
            else -> get(userId, id)?.mergeData(param) ?: throw400()
        }
        repo.save(note)
    }

    suspend fun delete(userId: Long, id: String) = tx {
        repo.deleteByUserIdAndId(userId, id)
    }
}