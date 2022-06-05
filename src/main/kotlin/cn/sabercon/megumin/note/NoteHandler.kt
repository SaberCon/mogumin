package cn.sabercon.megumin.note

import cn.sabercon.common.data.CREATED_AT
import cn.sabercon.common.data.desc
import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Service

@Service
class NoteHandler(private val repo: NoteRepo) {

    suspend fun get(userId: Long, id: String): Note? {
        return repo.findByUserIdAndId(userId, id)
    }

    fun list(userId: Long): Flow<Note> {
        return repo.findByUserId(userId, desc(CREATED_AT))
    }

    suspend fun insert(userId: Long, param: NoteParam) {
        val note = Note(
            userId = userId,
            title = param.title,
            content = param.content,
        )
        repo.save(note)
    }

    suspend fun update(userId: Long, id: String, param: NoteParam) {
        val note = Note(
            id = id,
            userId = userId,
            title = param.title,
            content = param.content,
        )
        repo.save(note)
    }

    suspend fun delete(userId: Long, id: String) {
        repo.deleteByUserIdAndId(userId, id)
    }
}
