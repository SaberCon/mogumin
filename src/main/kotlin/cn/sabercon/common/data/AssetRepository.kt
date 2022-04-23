package cn.sabercon.common.data

import cn.sabercon.common.web.Page
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.toList
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.repository.NoRepositoryBean
import org.springframework.data.repository.kotlin.CoroutineSortingRepository

@NoRepositoryBean
interface AssetRepository<T : Any> : CoroutineSortingRepository<T, String> {

    suspend fun findByUserIdAndId(userId: Long, id: String): T?

    fun findByUserId(userId: Long, sort: Sort): Flow<T>

    fun findByUserId(userId: Long, pageable: Pageable): Flow<T>

    suspend fun countByUserId(userId: Long): Long

    suspend fun findPageByUserId(userId: Long, pageable: Pageable) =
        Page(countByUserId(userId), findByUserId(userId, pageable).toList())

    suspend fun deleteByUserIdAndId(userId: Long, id: String)
}
