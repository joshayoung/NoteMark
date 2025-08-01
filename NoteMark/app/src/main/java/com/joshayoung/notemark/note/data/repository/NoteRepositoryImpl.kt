package com.joshayoung.notemark.note.data.repository

import android.provider.ContactsContract
import com.joshayoung.notemark.BuildConfig
import com.joshayoung.notemark.core.data.Error
import com.joshayoung.notemark.core.data.database.entity.NoteEntity
import com.joshayoung.notemark.auth.domain.models.Login
import com.joshayoung.notemark.auth.domain.models.LoginResponse
import com.joshayoung.notemark.note.domain.repository.NoteRepository
import com.joshayoung.notemark.auth.domain.models.Registration
import com.joshayoung.notemark.core.domain.DataStorage
import com.joshayoung.notemark.note.domain.database.LocalDataSource
import com.joshayoung.notemark.note.domain.models.Note
import com.joshayoung.notemark.note.domain.models.Notes
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.AuthCircuitBreaker
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.UUID

typealias Result = com.joshayoung.notemark.core.domain.Result

class NoteRepositoryImpl (
    private val client: HttpClient,
    private val dataStorage: DataStorage,
    private val localDataSource: LocalDataSource
) : NoteRepository {
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

    override suspend fun updateNote(note: Note?, title: String, body: String): Result {
        val updatedNote = Note(
            id = note?.id ?: "",
            title = title,
            content = body,
            createdAt = note!!.createdAt
        )
        val response : HttpResponse = client.put {
            url(BuildConfig.BASE_URL + BuildConfig.NOTE_PATH)
            setBody(updatedNote)
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

    override suspend fun getNotes(): Result {
        val result = localDataSource.upsertNotes(NoteEntity(
            content = "test",
            title = "test",
            createdAt = "test"
        ))
        val note = localDataSource.getNotes().first()

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

    override suspend fun deleteNote(id: String): Result {
        println("test")
        val url = BuildConfig.BASE_URL + BuildConfig.NOTE_PATH + "/" + id
        val response = client.delete {
            url(url)
//            parameter("id", id)
        }
        when (response.status) {
            HttpStatusCode.OK -> {
                val responseText = response.bodyAsText()
//                val jsonObject = Json.decodeFromString<Notes>(responseText)
                return Result(success = true)
            }
            else -> {
                val responseText = response.bodyAsText()
                val jsonObject = Json.decodeFromString<Error>(responseText)
                return Result(success = false)
            }
        }
    }
}