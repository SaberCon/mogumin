package cn.sabercon.common.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.kotlin.CoroutineSortingRepository

@NoRepositoryBean
interface AssetRepository<T : Any> : CoroutineSortingRepository<T, String> {

    suspend fun findByUserIdAndId(userId: Long, id: String): T?

    suspend fun deleteByUserIdAndId(userId: Long, id: String)

    fun findByUserId(userId: Long, sort: Sort): Flow<T>

    fun findByUserId(userId: Long, pageable: Pageable): Flow<T>

    suspend fun countByUserId(userId: Long): Long

    suspend fun findPageByUserId(userId: Long, pageable: Pageable): Page<T> {
        return PageImpl(findByUserId(userId, pageable).toList(), pageable, countByUserId(userId))
    }
}
