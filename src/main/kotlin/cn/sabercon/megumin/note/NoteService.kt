package cn.sabercon.megumin.note

import cn.sabercon.dgs.codegen.generated.types.CreateNoteInput
import cn.sabercon.dgs.codegen.generated.types.UpdateNoteInput
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class NoteService(private val repo: NoteRepo) {

    suspend fun get(userId: Long, id: String): Note? {
        return repo.findByUserIdAndId(userId, id)
    }

    suspend fun getPage(userId: Long, pageable: Pageable): Page<Note> {
        return repo.findPageByUserId(userId, pageable)
    }

    suspend fun create(userId: Long, input: CreateNoteInput): Note {
        return Note(
            userId = userId,
            title = input.title,
            content = input.content,
        ).let { repo.save(it) }
    }

    suspend fun update(userId: Long, input: UpdateNoteInput): Note? {
        return get(userId, input.id)?.run {
            copy(
                title = input.title ?: title,
                content = input.content ?: content,
            )
        }?.let { repo.save(it) }
    }

    suspend fun delete(userId: Long, id: String) {
        repo.deleteByUserIdAndId(userId, id)
    }
}
