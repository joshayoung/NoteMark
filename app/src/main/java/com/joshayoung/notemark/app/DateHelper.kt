package com.joshayoung.notemark.app

import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeParseException

// TODO: Move to Mapper:
class DateHelper {
    companion object Companion {
        fun convertToDate(dateString: String?): ZonedDateTime? {
            val instant = Instant.parse(dateString)
            val zonedDateTime = instant.atZone(ZoneId.of("UTC"))

            return try {
                zonedDateTime
            } catch (e: DateTimeParseException) {
                null
            }
        }
    }
}