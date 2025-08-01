package com.joshayoung.notemark.core.data

import com.joshayoung.notemark.BuildConfig
import com.joshayoung.notemark.auth.domain.models.LoginResponse
import com.joshayoung.notemark.auth.domain.models.RefreshToken
import com.joshayoung.notemark.core.domain.DataStorage
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json

class HttpClientProvider(
    private val sessionStorage: DataStorage
) {
    fun provide(): HttpClient {
        return HttpClient(CIO){
            install(Logging){
                level = LogLevel.ALL
            }

            install(ContentNegotiation){
                json(
                    json = Json {
                        ignoreUnknownKeys = true
                    }
                )
            }

            install(Auth) {
                bearer {
                    loadTokens {
                        val tokenPair = sessionStorage.getAuthData().first()
                        BearerTokens(
                            accessToken = tokenPair.accessToken ?: "",
                            refreshToken = tokenPair.refreshToken ?: ""
                        )
                    }

                    refreshTokens {
                        val tokenPair = sessionStorage.getAuthData().first()
                        val response = client.post {
                            url(BuildConfig.BASE_URL + BuildConfig.REFRESH_PATH)
                            setBody(RefreshToken(refreshToken = tokenPair.refreshToken ?: ""))
                            markAsRefreshTokenRequest()

                            // To invalidate token after 30 seconds:
                            header("Debug", true)
                        }

                        if (response.status == HttpStatusCode.OK) {
                            val responseText = response.bodyAsText()
                            val jsonObject = Json.decodeFromString<LoginResponse>(responseText)
                            sessionStorage.saveAuthData(jsonObject)

                            BearerTokens(
                                accessToken = jsonObject.accessToken ?: "",
                                refreshToken = jsonObject.refreshToken
                            )
                            // test expiration:
//                            sessionStorage.saveAuthData(LoginResponse(refreshToken = "", accessToken = "", username = ""))
//                            BearerTokens(
//                                accessToken = "",
//                                refreshToken = ""
//                            )
                        } else {
                            BearerTokens(
                                accessToken = "",
                                refreshToken = ""
                            )
                        }
                    }
                }

            }

            defaultRequest {
                contentType(ContentType.Application.Json)
                header("X-User-Email", BuildConfig.CAMPUS_SUBSCRIPTION_EMAIL)

                // To invalidate token after 30 seconds:
                header("Debug", true)
            }
        }
    }
}