package com.joshayoung.notemark.auth.domain.repository

import com.joshayoung.notemark.auth.domain.models.LoginResponse
import com.joshayoung.notemark.core.data.networking.DataError
import com.joshayoung.notemark.core.data.networking.EmptyResult
import com.joshayoung.notemark.core.data.networking.Result

interface AuthRepository {
    suspend fun register(
        username: String,
        email: String,
        password: String,
    ): EmptyResult<DataError.Network>

    suspend fun login(
        email: String,
        password: String,
    ): Result<LoginResponse, DataError>
}