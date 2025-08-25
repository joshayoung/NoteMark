package com.joshayoung.notemark.core.domain.use_cases

import com.joshayoung.notemark.auth.data.use_cases.ValidateEmail
import com.joshayoung.notemark.auth.data.use_cases.ValidatePassword
import com.joshayoung.notemark.auth.data.use_cases.ValidateUsername
import com.joshayoung.notemark.note.data.use_cases.PullRemoteNotesUseCase
import com.joshayoung.notemark.note.data.use_cases.SyncNotesUseCase

data class NoteMarkUseCases(
    val pullRemoteNotesUseCase: PullRemoteNotesUseCase,
    val validateEmail: ValidateEmail,
    val syncNotesUseCase: SyncNotesUseCase,
    val validatePassword: ValidatePassword,
    val validateUsername: ValidateUsername,
)