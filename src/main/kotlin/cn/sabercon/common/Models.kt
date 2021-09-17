package cn.sabercon.common

data class ErrorResponse<out T : Any>(val code: String, val msg: String, val data: T? = null)

data class PageData<out T : Any>(val total: Long, val list: List<T>)

val EMPTY_PAGE = PageData<Nothing>(0, emptyList())

