package com.joshayoung.notemark.auth.domain.repository

import com.joshayoung.notemark.core.data.networking.DataError
import com.joshayoung.notemark.core.data.networking.EmptyDataResult
import com.joshayoung.notemark.core.data.networking.EmptyResult

interface AuthRepository {
    suspend fun register(
        username: String,
        email: String,
        password: String,
    ): EmptyResult<DataError.Network>

    suspend fun login(
        email: String,
        password: String,
    ): EmptyDataResult<DataError.Network>
}