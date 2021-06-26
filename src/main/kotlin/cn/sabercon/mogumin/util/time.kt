package cn.sabercon.mogumin.util

import cn.sabercon.mogumin.base.wrapExceptionToNull
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

val DATETIME_FMT = DateTimeFormatter.ISO_DATE_TIME!!
val DATE_FMT = DateTimeFormatter.ISO_DATE!!
val TIME_FMT = DateTimeFormatter.ISO_TIME!!

fun String.toDatetime() = DATETIME_FMT.parse(this, LocalDateTime::from)!!
fun String.toDate() = DATE_FMT.parse(this, LocalDate::from)!!
fun String.toTime() = TIME_FMT.parse(this, LocalTime::from)!!
fun String.toDatetimeOrNull() = wrapExceptionToNull { toDatetime() }
fun String.toDateOrNull() = wrapExceptionToNull { toDate() }
fun String.toTimeOrNull() = wrapExceptionToNull { toTime() }

fun LocalDateTime.format() = format(DATETIME_FMT)!!
fun LocalDate.format() = format(DATE_FMT)!!
fun LocalTime.format() = format(TIME_FMT)!!