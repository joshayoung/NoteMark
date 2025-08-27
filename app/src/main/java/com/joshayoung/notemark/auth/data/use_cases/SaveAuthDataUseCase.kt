package com.joshayoung.notemark.auth.data.use_cases

import com.joshayoung.notemark.auth.domain.models.LoginResponse
import com.joshayoung.notemark.core.domain.AuthDataStorage

class SaveAuthDataUseCase(
    private val authDataStorage: AuthDataStorage,
) {
    suspend operator fun invoke(data: LoginResponse) {
//        authDataStorage.saveAuthData(
//            LoginResponse(
//                accessToken = data.accessToken,
//                refreshToken = data.refreshToken,
//                username = data.username,
//            ),
//        )
    }
}