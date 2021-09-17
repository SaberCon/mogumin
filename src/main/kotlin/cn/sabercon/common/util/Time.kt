package cn.sabercon.common.util

import java.time.*
import java.time.format.DateTimeFormatter

val DATETIME_FMT = DateTimeFormatter.ISO_DATE_TIME!!
val DATE_FMT = DateTimeFormatter.ISO_DATE!!
val TIME_FMT = DateTimeFormatter.ISO_TIME!!
val EPOCH = LocalDateTime.of(1970, 1, 1, 0, 0, 0)!!

fun String.toDatetime() = DATETIME_FMT.parse(this, LocalDateTime::from)!!
fun String.toDate() = DATE_FMT.parse(this, LocalDate::from)!!
fun String.toTime() = TIME_FMT.parse(this, LocalTime::from)!!
fun String.toDatetimeOrNull() = runCatching { toDatetime() }.getOrNull()
fun String.toDateOrNull() = runCatching { toDate() }.getOrNull()
fun String.toTimeOrNull() = runCatching { toTime() }.getOrNull()

fun LocalDateTime.format() = format(DATETIME_FMT)!!
fun LocalDate.format() = format(DATE_FMT)!!
fun LocalTime.format() = format(TIME_FMT)!!

val LocalDateTime.epochSeconds get() = toEpochSecond(ZoneOffset.UTC)

val Int.milliseconds get() = Duration.ofMillis(toLong())!!
val Int.seconds get() = Duration.ofSeconds(toLong())!!
val Int.minutes get() = Duration.ofMinutes(toLong())!!
val Int.hours get() = Duration.ofHours(toLong())!!
val Int.days get() = Duration.ofDays(toLong())!!

val now get() = LocalDateTime.now()!!