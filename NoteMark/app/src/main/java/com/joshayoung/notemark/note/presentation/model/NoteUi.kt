package com.joshayoung.notemark.note.presentation.model

import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

data class NoteUi(
    val id: Long? = null,
    val remoteId: String?,
    val title: String,
    val content: String?,
    val createdAt: String?,
    val lastEditedAt: String? = null,
) {
    val dateCreated: String get() = formatDate(createdAt)
    val dateLastEdited: String get() = formatDate(lastEditedAt)

    val date: String get() {
        val dateTime = OffsetDateTime.parse(createdAt)

        val currentYear = LocalDate.now().year
        var formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
        val year = dateTime.format(DateTimeFormatter.ofPattern("yyyy")).toInt()
        if (currentYear == year) {
            formatter = DateTimeFormatter.ofPattern("dd MMM")
        }

        val formatted = dateTime.format(formatter)

        return formatted.uppercase()
    }

    private fun formatDate(date: String?): String {
        if (date == null) {
            return ""
        }

        val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy, HH:mm")
        val d = OffsetDateTime.parse(date)

        val now = OffsetDateTime.now()
        val difference = ChronoUnit.MINUTES.between(d, now)

        if (difference < 5) {
            return "Just now"
        }

        return d.format(formatter)
    }
}