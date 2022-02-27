package cn.sabercon.common

data class Page<out T : Any>(val total: Long, val list: List<T>)

private val EMPTY_PAGE = Page<Nothing>(0, emptyList())

fun <T : Any> emptyPage(): Page<T> = EMPTY_PAGE

