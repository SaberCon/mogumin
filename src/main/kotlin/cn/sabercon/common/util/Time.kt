package cn.sabercon.common.util

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

fun String.toDatetime() = DateTimeFormatter.ISO_DATE_TIME.parse(this, LocalDateTime::from)!!

fun String.toDate() = DateTimeFormatter.ISO_DATE.parse(this, LocalDate::from)!!

fun String.toTime() = DateTimeFormatter.ISO_TIME.parse(this, LocalTime::from)!!

val EPOCH = LocalDateTime.of(1970, 1, 1, 0, 0, 0)!!

fun now() = LocalDateTime.now()!!

fun justNow() = now().minusMinutes(1)!!