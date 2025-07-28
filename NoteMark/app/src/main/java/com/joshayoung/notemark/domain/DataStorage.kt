package com.joshayoung.notemark.domain

import kotlinx.coroutines.flow.Flow

interface DataStorage {
    fun getAuthData(): Flow<LoginResponse>
    suspend fun saveAuthData(settings: LoginResponse?)
}