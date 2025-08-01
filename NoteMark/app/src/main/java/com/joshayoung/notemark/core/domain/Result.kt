package com.joshayoung.notemark.core.domain

import com.joshayoung.notemark.note.domain.models.Note
import com.joshayoung.notemark.note.domain.models.Notes

typealias Error = com.joshayoung.notemark.core.data.Error

data class Result(
    var success: Boolean,
    var error: Error? = null,
    val note: Note? = null,
    val notes: Notes? = null
)