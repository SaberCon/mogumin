package cn.sabercon.common.util

import java.time.OffsetDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

fun String.toDatetime() = DateTimeFormatter.ISO_DATE_TIME.parse(this, OffsetDateTime::from)!!

val EPOCH = OffsetDateTime.of(1970, 1, 1, 0, 0, 0, 0, ZoneOffset.UTC)!!

fun now() = OffsetDateTime.now()!!
