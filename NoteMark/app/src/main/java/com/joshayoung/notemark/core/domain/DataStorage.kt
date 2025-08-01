package com.joshayoung.notemark.core.domain

import com.joshayoung.notemark.auth.domain.models.LoginResponse
import kotlinx.coroutines.flow.Flow

interface DataStorage {
    fun getAuthData(): Flow<LoginResponse>
    suspend fun saveAuthData(settings: LoginResponse?)
    val values: Flow<String>
}