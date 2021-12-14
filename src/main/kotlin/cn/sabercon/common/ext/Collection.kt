package cn.sabercon.common.ext

// Array
inline fun <T, reified R> Array<out T>.mapArray(transform: (T) -> R) = map(transform).toTypedArray()