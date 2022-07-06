package cn.sabercon.common.graphql

import com.google.common.base.CaseFormat
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort

class OffsetPageRequest(private val offset: Long, page: Int, limit: Int, sort: Sort) : PageRequest(page, limit, sort) {

    override fun getOffset() = offset

    companion object {
        fun of(offset: Long, limit: Int, sort: Sort): OffsetPageRequest {
            require(offset >= 0)
            require(limit > 0)
            val page = (offset / limit).toInt()
            return OffsetPageRequest(offset, page, limit, sort)
        }
    }
}

data class ForwardPagination(
    val first: Int,
    val after: String?,
    val orderBy: RelayOrder,
) {

    fun toPageRequest(): OffsetPageRequest {
        val offset = after?.toLong() ?: 0
        return OffsetPageRequest.of(offset, first, orderBy.toSort())
    }
}

data class RelayOrder(
    val direction: Sort.Direction,
    val field: String,
) {

    fun toOrder(): Sort.Order = Sort.Order(direction, CONVERTER.convert(field)!!)

    fun toSort(): Sort = Sort.by(toOrder())

    companion object {
        private val CONVERTER = CaseFormat.UPPER_UNDERSCORE.converterTo(CaseFormat.LOWER_CAMEL)
    }
}

data class CountConnection<out T : Any>(
    val edges: List<Edge<T>>,
    val nodes: List<T>,
    val pageInfo: PageInfo,
    val totalCount: Long,
) {

    data class Edge<out T : Any>(
        val node: T,
        val cursor: String,
    )

    data class PageInfo(
        val startCursor: String?,
        val endCursor: String?,
        val hasPreviousPage: Boolean,
        val hasNextPage: Boolean,
    )

    companion object {
        fun <T : Any> fromForwardPage(page: Page<T>): CountConnection<T> {
            val offset = page.pageable.offset
            val edges = page.content.mapIndexed { index, node -> Edge(node, (offset + index + 1).toString()) }
            val pageInfo = PageInfo(
                startCursor = edges.firstOrNull()?.cursor,
                endCursor = edges.lastOrNull()?.cursor,
                hasPreviousPage = offset > 0,
                hasNextPage = offset + edges.size < page.totalElements,
            )
            return CountConnection(edges, page.content, pageInfo, page.totalElements)
        }
    }
}
