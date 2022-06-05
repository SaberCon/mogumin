package cn.sabercon.common.model

data class Page<out T : Any>(val total: Long, val list: List<T>) {

    companion object {

        val EMPTY: Page<Nothing> = Page(0, emptyList())
    }
}
