package com.joshayoung.notemark.note.presentation.note_list.model

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class NoteUiTest {
    private lateinit var noteUi: NoteUi

    @BeforeEach
    fun setUp() {
        noteUi = NoteUi(
            id = "1",
            title = "First Note",
            content = "First Note Content",
            createdAt = "2025-07-26T16:15:05+00:00",
            lastEditedAt = "2025-07-26T16:17:05+00:00"
        )
    }

    @Test
    fun `Date is formatted correctly for short month name`() {
        noteUi = noteUi.copy(createdAt = "2025-07-26T16:17:05+00:00")
        val formattedDate = noteUi.date

        assertThat(formattedDate).isEqualTo("26 JUL")
    }

    @Test
    fun `Date is formatted correctly for long month name`() {
        noteUi = noteUi.copy(createdAt = "2025-01-03T16:17:05+00:00")
        val formattedDate = noteUi.date

        assertThat(formattedDate).isEqualTo("03 JAN")
    }

    @Test
    fun `Date is formatted correctly for date last year`() {
        noteUi = noteUi.copy(createdAt = "2024-12-26T16:17:05+00:00")
        val formattedDate = noteUi.date

        assertThat(formattedDate).isEqualTo("26 DECEMBER 2024")
    }
}