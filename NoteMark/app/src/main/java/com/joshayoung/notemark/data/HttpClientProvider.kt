package com.joshayoung.notemark.data

import com.joshayoung.notemark.BuildConfig
import com.joshayoung.notemark.domain.LoginResponse
import com.joshayoung.notemark.domain.RefreshToken
import com.joshayoung.notemark.domain.SessionStorage
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
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import kotlin.Result
import kotlin.text.set

class HttpClientProvider(
    private val sessionStorage: SessionStorage
) {
    fun provide(): HttpClient {
        return HttpClient(CIO){
            install(ContentNegotiation){
                json()
            }
            install(Logging){
                level = LogLevel.ALL
            }

            install(Auth) {
                bearer {
                    loadTokens {
                        val tokenPair = sessionStorage.get()
                        BearerTokens(
                            accessToken = tokenPair?.accessToken ?: "",
                            refreshToken = tokenPair?.refreshToken ?: ""
                        )
                    }

                    refreshTokens {
                        val tokenPair = sessionStorage.get()
                        val response = client.post(
                            urlString = BuildConfig.BASE_URL + BuildConfig.REFRESH_PATH
                        ) {
                            contentType(ContentType.Application.Json)
                            setBody(RefreshToken(
                                refreshToken = tokenPair?.refreshToken ?: ""))

                                // To invalidate token after 30 seconds:
                                header("Debug", true)
                        }

                        val responseText = response.bodyAsText()
                        val jsonObject = Json.decodeFromString<LoginResponse>(responseText)
                        if (response.status == HttpStatusCode.OK) {
                            sessionStorage.set(jsonObject)

                            BearerTokens(
                                accessToken = jsonObject.accessToken,
                                refreshToken = jsonObject.refreshToken
                            )
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
//                header("Debug", true)
            }
        }
    }
}