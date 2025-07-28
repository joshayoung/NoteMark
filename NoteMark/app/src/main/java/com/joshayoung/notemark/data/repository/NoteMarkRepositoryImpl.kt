package com.joshayoung.notemark.data.repository

import com.joshayoung.notemark.BuildConfig
import com.joshayoung.notemark.data.Error
import com.joshayoung.notemark.domain.models.Login
import com.joshayoung.notemark.domain.LoginResponse
import com.joshayoung.notemark.domain.repository.NoteMarkRepository
import com.joshayoung.notemark.domain.models.Registration
import com.joshayoung.notemark.domain.DataStorage
import com.joshayoung.notemark.domain.models.Note
import com.joshayoung.notemark.domain.models.Notes
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.AuthCircuitBreaker
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.json.Json
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

typealias Result = com.joshayoung.notemark.domain.models.Result

class NoteMarkRepositoryImpl (
    private val client: HttpClient,
    private val dataStorage: DataStorage
) : NoteMarkRepository {
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
    ): com.joshayoung.notemark.domain.models.Result {
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

    override suspend fun createNote(title: String, body: String): Result {
        val currentDateTime = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val formattedDateTime = currentDateTime.format(formatter)
        val note = Note(
            id = UUID.randomUUID().toString(),
            title = title,
            content = body,
            createdAt = formattedDateTime
        )
        val response = client.post {
            url(BuildConfig.BASE_URL + BuildConfig.NOTE_PATH)
            setBody(note)
        }

        return when (response.status) {
            HttpStatusCode.OK -> {
                val responseText = response.bodyAsText()
                val jsonObject = Json.decodeFromString<Note>(responseText)
                Result(success = true, note = jsonObject)
            }
            else -> {
                val responseText = response.bodyAsText()
                val jsonObject = Json.decodeFromString<Error>(responseText)
                // TODO: Make this more robust:
                Result(success = false, error = jsonObject)
            }
        }
    }

    override suspend fun updateNote(note: Note): Result {
        TODO("Not yet implemented")
    }

    override suspend fun getNotes(): Result {
        val response = client.get {
            url(BuildConfig.BASE_URL + BuildConfig.NOTE_PATH)
        }

        when (response.status) {
            HttpStatusCode.OK -> {
                val responseText = response.bodyAsText()
                val jsonObject = Json.decodeFromString<Notes>(responseText)
                return Result(success = true, notes = jsonObject)
            }
            else -> {
                return Result(success = false)
            }
        }
    }

    override suspend fun deleteNote(note: Note): Result {
        TODO("Not yet implemented")
    }
}