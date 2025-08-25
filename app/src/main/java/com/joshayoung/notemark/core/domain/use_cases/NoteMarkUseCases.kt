package com.joshayoung.notemark.core.domain.use_cases

import com.joshayoung.notemark.auth.data.use_cases.SaveAuthDataUseCase
import com.joshayoung.notemark.auth.data.use_cases.ValidateEmailUseCase
import com.joshayoung.notemark.auth.data.use_cases.ValidatePasswordUseCase
import com.joshayoung.notemark.auth.data.use_cases.ValidateUsernameUseCase
import com.joshayoung.notemark.note.data.use_cases.ClearLocalDataUseCase
import com.joshayoung.notemark.note.data.use_cases.LoggedInUserAbbreviationUseCase
import com.joshayoung.notemark.note.data.use_cases.PullRemoteNotesUseCase
import com.joshayoung.notemark.note.data.use_cases.SetUserIdUseCase
import com.joshayoung.notemark.note.data.use_cases.SyncNotesUseCase

data class NoteMarkUseCases(
    val pullRemoteNotesUseCase: PullRemoteNotesUseCase,
    val validateEmail: ValidateEmailUseCase,
    val syncNotesUseCase: SyncNotesUseCase,
    val validatePasswordUseCase: ValidatePasswordUseCase,
    val validateUsernameUseCase: ValidateUsernameUseCase,
    val loggedInUserAbbreviationUseCase: LoggedInUserAbbreviationUseCase,
    val clearLocalDataUseCase: ClearLocalDataUseCase,
    val setUserIdUseCase: SetUserIdUseCase,
    val saveAuthDataUseCase: SaveAuthDataUseCase,
)