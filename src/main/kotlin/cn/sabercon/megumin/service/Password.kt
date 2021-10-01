package cn.sabercon.megumin.service

import cn.sabercon.common.BaseCode
import cn.sabercon.common.data.pageable
import cn.sabercon.common.util.copyFromModel
import cn.sabercon.common.util.copyToModel
import cn.sabercon.common.util.getCurrentUserId
import cn.sabercon.megumin.model.Password
import cn.sabercon.megumin.model.PasswordParam
import cn.sabercon.megumin.repo.PasswordRepo
import kotlinx.coroutines.flow.Flow
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PasswordService(private val repo: PasswordRepo) {

    suspend fun get(id: String): Password? {
        return repo.findByUserIdAndId(getCurrentUserId(), id)
    }

    suspend fun list(): Flow<Password> {
        return repo.findByUserId(getCurrentUserId(), pageable())
    }

    @Transactional
    suspend fun save(param: PasswordParam) {
        val password = when {
            param.id.isEmpty() -> param.copyToModel(Password::userId to getCurrentUserId())
            else -> get(param.id)?.copyFromModel(param) ?: BaseCode.BAD_REQUEST.throws()
        }
        repo.save(password)
    }

    @Transactional
    suspend fun delete(ids: List<String>) {
        repo.deleteByUserIdAndIdIn(getCurrentUserId(), ids)
    }
}