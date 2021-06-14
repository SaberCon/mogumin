package cn.sabercon.mogumin.util

import cn.sabercon.mogumin.base.wrapExceptionToNull
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter.ofPattern

val DATETIME_FMT = ofPattern("yyyy-MM-dd HH:mm:ss")!!
val DATE_FMT = ofPattern("yyyy-MM-dd")!!
val TIME_FMT = ofPattern("HH:mm:ss")!!

fun String.toDatetime() = DATETIME_FMT.parse(this, LocalDateTime::from)!!
fun String.toDate() = DATE_FMT.parse(this, LocalDate::from)!!
fun String.toTime() = TIME_FMT.parse(this, LocalTime::from)!!
fun String.toDatetimeOrNull() = wrapExceptionToNull { toDatetime() }
fun String.toDateOrNull() = wrapExceptionToNull { toDate() }
fun String.toTimeOrNull() = wrapExceptionToNull { toTime() }

fun LocalDateTime.format() = format(DATETIME_FMT)!!
fun LocalDate.format() = format(DATE_FMT)!!
fun LocalTime.format() = format(TIME_FMT)!!