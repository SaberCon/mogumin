package cn.sabercon.megumin.service

import cn.sabercon.common.BaseCode
import cn.sabercon.common.data.pageable
import cn.sabercon.common.util.copyFromModel
import cn.sabercon.common.util.copyToModel
import cn.sabercon.common.util.getCurrentUserId
import cn.sabercon.megumin.model.Note
import cn.sabercon.megumin.model.NoteParam
import cn.sabercon.megumin.repo.NoteRepo
import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class NoteService(private val repo: NoteRepo) {

    suspend fun get(id: String): Note? {
        return repo.findByIdAndUserId(id, getCurrentUserId())
    }

    suspend fun list(): Flow<Note> {
        return repo.findByUserId(getCurrentUserId(), pageable())
    }

    @Transactional
    suspend fun save(param: NoteParam) {
        val note = when {
            param.id.isEmpty() -> param.copyToModel(Note::userId to getCurrentUserId())
            else -> get(param.id)?.copyFromModel(param) ?: BaseCode.BAD_REQUEST.throws()
        }
        repo.save(note)
    }

    @Transactional
    suspend fun delete(ids: List<String>) {
        repo.deleteByIdInAndUserId(ids, getCurrentUserId())
    }
}