package cn.sabercon.megumin.note

import cn.sabercon.common.data.CTIME
import cn.sabercon.common.data.desc
import cn.sabercon.common.data.r2dbc.tx
import cn.sabercon.common.throwClientError
import cn.sabercon.common.util.convertData
import cn.sabercon.common.util.mergeData
import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Service

@Service
class NoteHandler(private val repo: NoteRepo) {

    suspend fun get(userId: Long, id: String): Note {
        return repo.findByUserIdAndId(userId, id) ?: throwClientError("Note does not exist")
    }

    fun list(userId: Long): Flow<Note> {
        return repo.findByUserId(userId, desc(CTIME))
    }

    suspend fun save(userId: Long, param: NoteParam) = tx {
        val note = when (val id = param.id) {
            "" -> param.convertData(Note::userId to userId)
            else -> get(userId, id).mergeData(param)
        }
        repo.save(note)
    }

    suspend fun delete(userId: Long, id: String) = tx {
        repo.deleteByUserIdAndId(userId, id)
    }
}