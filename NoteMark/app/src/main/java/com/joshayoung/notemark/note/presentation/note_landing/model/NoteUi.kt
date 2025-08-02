package com.joshayoung.notemark.note.presentation.note_landing.model

import java.time.LocalDate
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter


data class NoteUi(
    val id: String? = null,
    val title: String,
    val content: String,
    val createdAt: String,
    val lastEditedAt: String? = null
) {

    val date: String get() {
        val dateTime = OffsetDateTime.parse(createdAt)

        val currentYear = LocalDate.now().year
        var formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy")
        val year =  dateTime.format(DateTimeFormatter.ofPattern("yyyy")).toInt()
        if (currentYear == year) {
            formatter = DateTimeFormatter.ofPattern("dd MMM")
        }

        val formatted =  dateTime.format(formatter)

        return formatted.uppercase()
    }
}