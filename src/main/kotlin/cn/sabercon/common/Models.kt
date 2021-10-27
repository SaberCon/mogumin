package cn.sabercon.common

data class ErrorResponse<out T : Any>(val code: String, val msg: String?, val data: T? = null)

data class Page<out T : Any>(val total: Long, val list: List<T>)

val EMPTY_PAGE = Page<Nothing>(0, emptyList())

