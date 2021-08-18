package cn.sabercon.mogumin.base

data class Result<out T : Any>(
    val ok: Boolean = true,
    val code: String? = null,
    val msg: String? = null,
    val debugMsg: String? = null,
    val data: T? = null,
)

fun <T : Any> emptyResult(): Result<T> = EMPTY_RESULT

val EMPTY_RESULT = Result<Nothing>()

data class Page<out T : Any>(
    val total: Long,
    val list: List<T>,
)

fun <T : Any> emptyPage(): Page<T> = EMPTY_PAGE

val EMPTY_PAGE = Page<Nothing>(0, emptyList())

