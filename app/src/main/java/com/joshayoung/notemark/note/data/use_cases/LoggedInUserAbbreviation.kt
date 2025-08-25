package com.joshayoung.notemark.note.data.use_cases

import com.joshayoung.notemark.core.domain.DataStorage
import kotlinx.coroutines.flow.first

class LoggedInUserAbbreviation(
    private val dataStorage: DataStorage,
) {
    suspend operator fun invoke(): String {
        val user = dataStorage.username.first()

        return formatUser(user)
    }

    private fun formatUser(user: String): String {
        if (user.length > 1) {
            val firstTwo = user.take(2)

            return firstTwo.uppercase()
        }

        return user.uppercase()
    }
}