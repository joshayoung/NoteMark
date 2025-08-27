package com.joshayoung.notemark.auth.data.use_cases

import com.joshayoung.notemark.auth.domain.models.LoginResponse
import com.joshayoung.notemark.core.domain.DataStorage

class SaveAuthDataUseCase(
    private val dataStorage: DataStorage,
) {
    suspend operator fun invoke(data: LoginResponse) {
        dataStorage.saveAuthData(
            LoginResponse(
                accessToken = data.accessToken,
                refreshToken = data.refreshToken,
                username = data.username,
            ),
        )
    }
}