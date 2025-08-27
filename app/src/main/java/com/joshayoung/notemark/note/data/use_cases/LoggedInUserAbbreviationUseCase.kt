package com.joshayoung.notemark.note.data.use_cases

import com.joshayoung.notemark.core.domain.AuthDataStorage

class LoggedInUserAbbreviationUseCase(
    private val authDataStorage: AuthDataStorage,
) {
    suspend operator fun invoke(): String {
//        val user = authDataStorage.username.first()
        val user = ""

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