package com.joshayoung.notemark.note.data.use_cases

import com.joshayoung.notemark.core.domain.NoteDataStorage

class SetUserIdUseCase(
    private val noteDataStorage: NoteDataStorage,
) {
    suspend operator fun invoke() {
//        val user = noteDataStorage.getUserid()
//        if (user == null) {
//            noteDataStorage.saveUserId(UUID.randomUUID().toString())
//        }
    }
}