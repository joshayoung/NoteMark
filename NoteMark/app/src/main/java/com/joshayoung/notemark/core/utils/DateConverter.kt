package com.joshayoung.notemark.core.utils

import java.time.LocalDateTime
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter


fun getTimeStampForInsert() : String {
    val currentDateTime = ZonedDateTime.now(java.time.ZoneOffset.UTC)
    val formatter = DateTimeFormatter.ISO_INSTANT
    val formattedDateTime = currentDateTime.format(formatter)
    val date = formattedDateTime.toString()

    return date;
}