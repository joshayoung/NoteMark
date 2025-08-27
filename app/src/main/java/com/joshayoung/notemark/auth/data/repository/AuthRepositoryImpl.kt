package com.joshayoung.notemark.auth.data.repository

import com.joshayoung.notemark.BuildConfig
import com.joshayoung.notemark.auth.domain.models.LoginRequest
import com.joshayoung.notemark.auth.domain.models.LoginResponse
import com.joshayoung.notemark.auth.domain.models.RegistrationRequest
import com.joshayoung.notemark.auth.domain.repository.AuthRepository
import com.joshayoung.notemark.core.data.networking.DataError
import com.joshayoung.notemark.core.data.networking.EmptyResult
import com.joshayoung.notemark.core.data.networking.Result
import com.joshayoung.notemark.core.data.networking.catchErrors
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.auth.AuthCircuitBreaker
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse

class AuthRepositoryImpl(
    private val client: HttpClient,
) : AuthRepository {
    override suspend fun register(
        username: String,
        email: String,
        password: String,
    ): EmptyResult<DataError.Network> =
        catchErrors {
            client.post {
                url(BuildConfig.BASE_URL + BuildConfig.REGISTER_PATH)
                setBody(RegistrationRequest(username = username, email = email, password = password))
            }
        }

    // TODO: Update return value to return data on success
    override suspend fun login(
        email: String,
        password: String,
    ): Result<LoginResponse, DataError> {
        val response =
            catchErrors<HttpResponse> {
                client.post {
                    url(BuildConfig.BASE_URL + BuildConfig.LOGIN_PATH)
                    setBody(LoginRequest(email = email, password = password))
                    attributes.put(AuthCircuitBreaker, Unit)
                }
            }

        if (response is Result.Success) {
            // TODO: Build an extension method to do this for you:
            val data = response.data.body<LoginResponse>()

            return Result.Success(data = data)
            // TODO: Use Case
        }

        // TODO: Correct this error response:
        return Result.Error(error = DataError.Network.UNKNOWN)
    }
}