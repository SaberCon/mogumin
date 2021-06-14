package cn.sabercon.mogumin.base

data class Result<T>(
    val ok: Boolean = true,
    val code: String? = null,
    val msg: String? = null,
    val debugMsg: String? = null,
    val data: T? = null,
)

data class PageModel<T>(
    val total: Long,
    val list: List<T>,
)