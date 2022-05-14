package cn.sabercon.common.web

data class Page<out T : Any>(val total: Long, val list: List<T>) {
    companion object {
        private val EMPTY_PAGE: Page<Nothing> = Page(0, emptyList())

        fun <T : Any> empty(): Page<T> = EMPTY_PAGE
    }
}
