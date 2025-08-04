package com.joshayoung.notemark.note.data.network

import com.joshayoung.notemark.BuildConfig
import com.joshayoung.notemark.core.domain.models.Error
import com.joshayoung.notemark.note.data.mappers.toNoteDto
import com.joshayoung.notemark.note.domain.models.Note
import com.joshayoung.notemark.note.domain.models.NotesData
import com.joshayoung.notemark.note.network.NoteDto
import com.joshayoung.notemark.note.network.RemoteDataSource
import com.joshayoung.notemark.note.network.toNote
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.json.Json

typealias Result = com.joshayoung.notemark.core.domain.models.Result

class KtorRemoteDataSource(
    private val client: HttpClient,
) : RemoteDataSource {
    override suspend fun saveNote(note: Note): Result {
        val noteDto = note.toNoteDto()
        val response = client.post {
            url(BuildConfig.BASE_URL + BuildConfig.NOTE_PATH)
            setBody(noteDto)
        }

        return when (response.status) {
            HttpStatusCode.OK -> {
                val responseText = response.bodyAsText()
                val jsonObject = Json.decodeFromString<NoteDto>(responseText)
                Result(success = true, note = jsonObject.toNote())
            }
            else -> {
                val responseText = response.bodyAsText()
                val jsonObject = Json.decodeFromString<Error>(responseText)
                // TODO: Make this more robust:
                Result(success = false, error = jsonObject)
            }
        }
    }

    override suspend fun getNotes(): com.joshayoung.notemark.core.domain.models.Result {
        val response = client.get {
            url(BuildConfig.BASE_URL + BuildConfig.NOTE_PATH)
        }

        when (response.status) {
            HttpStatusCode.OK -> {
                val responseText = response.bodyAsText()
                val jsonObject = Json.decodeFromString<NotesData>(responseText)
                return Result(success = true, notes = jsonObject.notes.map { n ->
                    n.toNote()
                })
            }
            else -> {
                return Result(success = false)
            }
        }
    }
}