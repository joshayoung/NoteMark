package com.joshayoung.notemark.auth.data.repository

import com.joshayoung.notemark.BuildConfig
import com.joshayoung.notemark.core.domain.models.Error
import com.joshayoung.notemark.auth.domain.models.Login
import com.joshayoung.notemark.auth.domain.models.LoginResponse
import com.joshayoung.notemark.auth.domain.models.Registration
import com.joshayoung.notemark.auth.domain.repository.AuthRepository
import com.joshayoung.notemark.core.data.networking.catchErrors
import com.joshayoung.notemark.core.domain.DataStorage
import com.joshayoung.notemark.core.domain.util.DataError
import com.joshayoung.notemark.core.domain.util.EmptyResult
import com.joshayoung.notemark.core.domain.util.Result
import com.joshayoung.notemark.core.domain.util.asEmptyDataResult
import com.joshayoung.notemark.note.domain.database.LocalDataSource
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.AuthCircuitBreaker
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.json.Json

class AuthRepositoryImpl (
    private val client: HttpClient,
    private val dataStorage: DataStorage
) : AuthRepository {
    override suspend fun register(username: String, email: String, password: String) : EmptyResult<DataError.Network> {
        return catchErrors {
            client.post {
                url(BuildConfig.BASE_URL + BuildConfig.REGISTER_PATH)
                setBody(Registration(username = username, email = email, password = password))
            }
        }
    }

    override suspend fun login(
        email: String,
        password: String
    ): EmptyResult<DataError.Network> {
            val response =
                catchErrors<HttpResponse> {
                    client.post {
                        url(BuildConfig.BASE_URL + BuildConfig.LOGIN_PATH)
                        setBody(Login(email = email, password = password))
                        attributes.put(AuthCircuitBreaker, Unit)
                    }
                }

        if (response is Result.Success) {
            // TODO: Build an extension method to do this for you:
            val r = response.data.body<LoginResponse>()

            dataStorage.saveAuthData(
                LoginResponse(
                    accessToken = r.accessToken,
                    refreshToken = r.refreshToken,
                    username = r.username
                )
            )
        }

        return response.asEmptyDataResult()
    }
}