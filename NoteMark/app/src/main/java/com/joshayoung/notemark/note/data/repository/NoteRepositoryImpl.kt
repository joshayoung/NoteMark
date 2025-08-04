package com.joshayoung.notemark.note.data.repository

import com.joshayoung.notemark.BuildConfig
import com.joshayoung.notemark.core.domain.models.Error
import com.joshayoung.notemark.note.data.network.KtorRemoteDataSource
import com.joshayoung.notemark.note.domain.database.LocalDataSource
import com.joshayoung.notemark.note.domain.models.Note
import com.joshayoung.notemark.note.domain.repository.NoteRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.delete
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.serialization.json.Json

typealias Result = com.joshayoung.notemark.core.domain.models.Result

class NoteRepositoryImpl (
    private val client: HttpClient,
    private val localDataSource: LocalDataSource,
    private val applicationScope: CoroutineScope,
    private val remoteDataSource: KtorRemoteDataSource
) : NoteRepository {
    override suspend fun createNote(note: Note): Result {
        val localResult = localDataSource.upsertNote(note)

        // TODO: Handle this case:
        if (!localResult.success) {
            return localResult
        }

        return applicationScope.async {
            val noteSave = remoteDataSource.saveNote(note)

            // TODO: Handle the failure:
            if (!noteSave.success) {
                return@async return@async Result(success = false)
            }

            return@async Result(success = true)
        }.await()
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
        return Result(success = false)
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