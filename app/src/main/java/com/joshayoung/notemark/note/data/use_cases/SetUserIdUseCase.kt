package com.joshayoung.notemark.note.data.use_cases

import com.joshayoung.notemark.core.domain.DataStorage
import java.util.UUID

class SetUserIdUseCase(
    private val dataStorage: DataStorage,
) {
    suspend operator fun invoke() {
        val user = dataStorage.getUserid()
        if (user == null) {
            dataStorage.saveUserId(UUID.randomUUID().toString())
        }
    }
}