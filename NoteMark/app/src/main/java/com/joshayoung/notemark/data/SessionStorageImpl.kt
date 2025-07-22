package com.joshayoung.notemark.data

import android.content.SharedPreferences
import com.joshayoung.notemark.domain.LoginResponse
import com.joshayoung.notemark.domain.SessionStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.slf4j.helpers.Reporter.info
import androidx.core.content.edit

class SessionStorageImpl(
    private val sharedPreferences: SharedPreferences
) : SessionStorage {
    override suspend fun get(): LoginResponse? {
        return withContext(Dispatchers.IO) {
            val json = sharedPreferences.getString(KEY_AUTH_INFO, null)
            json?.let {
                Json.decodeFromString<LoginResponse>(it)
            }
        }
    }

    override suspend fun set(response: LoginResponse?) {
        // make this blocking:
        // we use the IO Dispatcher because this is an IO operation:
        withContext(Dispatchers.IO) {
            if (response == null) {
                sharedPreferences.edit(commit = true) { remove(KEY_AUTH_INFO) }

                return@withContext
            }

            val json = Json.encodeToString(response)
            sharedPreferences
                .edit(commit = true) {
                    putString(KEY_AUTH_INFO, json)
                }

        }
    }

    companion object {
        private const val KEY_AUTH_INFO = "KEY_AUTH_INFO"
    }
}