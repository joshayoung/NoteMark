package com.joshayoung.notemark.auth.data.repository

import com.joshayoung.notemark.BuildConfig
import com.joshayoung.notemark.core.domain.models.Error
import com.joshayoung.notemark.auth.domain.models.Login
import com.joshayoung.notemark.auth.domain.models.LoginResponse
import com.joshayoung.notemark.auth.domain.models.Registration
import com.joshayoung.notemark.auth.domain.repository.AuthRepository
import com.joshayoung.notemark.core.domain.DataStorage
import com.joshayoung.notemark.note.domain.database.LocalDataSource
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.AuthCircuitBreaker
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.json.Json

typealias Result = com.joshayoung.notemark.core.domain.models.Result

class AuthRepositoryImpl (
    private val client: HttpClient,
    private val dataStorage: DataStorage,
    private val localDataSource: LocalDataSource
) : AuthRepository {
    override suspend fun register(username: String, email: String, password: String) : Result {
        val response : HttpResponse = client.post {
            url(BuildConfig.BASE_URL + BuildConfig.REGISTER_PATH)
            setBody(Registration(username = username, email = email, password = password))
        }

        return when (response.status) {
            HttpStatusCode.OK -> {
                Result(success = true)
            }
            else -> {
                val responseText = response.bodyAsText()
                val jsonObject = Json.decodeFromString<Error>(responseText)
                // TODO: Make this more robust:
                Result(success = false, error = jsonObject)
            }
        }
    }

    override suspend fun login(
        email: String,
        password: String
    ): com.joshayoung.notemark.core.domain.models.Result {
        try {
            val response = client.post {
                url(BuildConfig.BASE_URL + BuildConfig.LOGIN_PATH)
                setBody(Login(email = email, password = password))
                attributes.put(AuthCircuitBreaker, Unit)
            }
            when (response.status) {
                HttpStatusCode.Unauthorized -> {
                    return Result(success = false)
                }
                HttpStatusCode.OK -> {
                    val responseText = response.bodyAsText()
                    // TODO: Use this:
                    val jsonObject = Json.decodeFromString<LoginResponse>(responseText)
                    dataStorage.saveAuthData(
                        LoginResponse(
                            accessToken = jsonObject.accessToken,
                            refreshToken = jsonObject.refreshToken,
                            username = jsonObject.username
                        )
                    )
                    return Result(success = true)
                }

                else -> {
//                    val responseText = response.bodyAsText()
//                    val jsonObject = Json.decodeFromString<Error>(responseText)
                    return Result(success = false)//, error = jsonObject)
                }
            }
        } catch(e: Exception) {
            return Result(success = false)
        }
    }
}