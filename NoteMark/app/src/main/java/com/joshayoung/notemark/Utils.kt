package com.joshayoung.notemark

import com.joshayoung.notemark.core.navigation.Destination
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException

class AuthHelper {
    companion object Companion {
        fun notInAuthRoutes(path: String?): Boolean {
            val route = path?.split('.')?.lastOrNull()
            val authRoutes = listOf(
                null,
                Destination.Login.toString(),
                Destination.StartScreen.toString(),
                Destination.Registration.toString()
            )

            return !authRoutes.contains(route)
        }
    }
}

class DateHelper {
    companion object Companion {
        fun convertToDate(dateString: String?) : ZonedDateTime? {
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