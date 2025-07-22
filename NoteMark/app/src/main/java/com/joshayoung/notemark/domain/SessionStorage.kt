package com.joshayoung.notemark.domain

interface SessionStorage {
    suspend fun get(): LoginResponse?
    suspend fun set(response: LoginResponse?)
}