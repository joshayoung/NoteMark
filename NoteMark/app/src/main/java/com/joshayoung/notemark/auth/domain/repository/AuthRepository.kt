package com.joshayoung.notemark.auth.domain.repository

import com.joshayoung.notemark.core.domain.util.DataError
import com.joshayoung.notemark.core.domain.util.EmptyDataResult
import com.joshayoung.notemark.core.domain.util.EmptyResult

interface AuthRepository {
    suspend fun register(username: String, email: String, password: String): EmptyResult<DataError.Network>
    suspend fun login(email: String, password: String): EmptyDataResult<DataError.Network>
}