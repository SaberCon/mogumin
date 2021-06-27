package cn.sabercon.mogumin.base

data class Result<T : Any>(
    val ok: Boolean = true,
    val code: String? = null,
    val msg: String? = null,
    val debugMsg: String? = null,
    val data: T? = null,
)

@Suppress("UNCHECKED_CAST")
fun <T : Any> emptyResult(): Result<T> = EMPTY_RESULT as Result<T>

val EMPTY_RESULT = Result<Nothing>()

data class Page<T : Any>(
    val total: Long,
    val list: List<T>,
)

@Suppress("UNCHECKED_CAST")
fun <T : Any> emptyPage(): Page<T> = EMPTY_PAGE as Page<T>

val EMPTY_PAGE = Page<Nothing>(0, emptyList())

