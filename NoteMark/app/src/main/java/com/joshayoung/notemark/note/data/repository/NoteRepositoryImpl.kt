package com.joshayoung.notemark.note.data.repository

import com.joshayoung.notemark.BuildConfig
import com.joshayoung.notemark.core.domain.models.Error
import com.joshayoung.notemark.core.domain.util.DataError
import com.joshayoung.notemark.core.domain.util.EmptyDataResult
import com.joshayoung.notemark.core.domain.util.EmptyResult
import com.joshayoung.notemark.core.domain.util.Result
import com.joshayoung.notemark.core.domain.util.asEmptyDataResult
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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMap
import kotlinx.coroutines.flow.toList
import kotlinx.serialization.json.Json


class NoteRepositoryImpl (
    private val client: HttpClient,
    private val localDataSource: LocalDataSource,
    private val applicationScope: CoroutineScope,
    private val remoteDataSource: KtorRemoteDataSource
) : NoteRepository {
    override suspend fun createNote(note: Note): EmptyDataResult<DataError> {
        val localResult = localDataSource.upsertNote(note)

        return if (localResult is Result.Success) {
    //        return applicationScope.async {
    //            val noteSave = remoteDataSource.saveNote(note)
    //
    //            // TODO: Handle the failure:
    //            if (!noteSave.success) {
    //                return@async return@async Result(success = false)
    //            }
    //
    //            return@async Result(success = true)
    //        }.await()
            Result.Success(Unit)
        } else {
            localResult.asEmptyDataResult()
        }
    }
//
    override suspend fun updateNote(note: Note?, title: String, body: String): EmptyResult<DataError> {
        val updatedNote = Note(
            id = note?.id,
            title = title,
            content = body,
            createdAt = note!!.createdAt
        )
        val result = localDataSource.upsertNote(updatedNote)

        if (result is Result.Success) {
            return Result.Success(Unit)
        } else {
            return result.asEmptyDataResult();
        }

//    val response : HttpResponse =
//        client.put {
//            url(BuildConfig.BASE_URL + BuildConfig.NOTE_PATH)
//            setBody(updatedNote)
//        }
//
//        return Result.Success(data = Unit)
//
//        return when (response.status) {
//            HttpStatusCode.OK -> {
//                Result(success = true)
//            }
//            else -> {
//                val responseText = response.bodyAsText()
//                val jsonObject = Json.decodeFromString<Error>(responseText)
//                // TODO: Make this more robust:
//                Result(success = false, error = jsonObject)
//            }
//        }
    }
//
    override suspend fun getNotes(): Flow<List<Note>> {
        return localDataSource.getNotes()
    }
//
    override suspend fun deleteNote(id: Int) {
        localDataSource.deleteNote(id)
    }
//
////        println("test")
////        val url = BuildConfig.BASE_URL + BuildConfig.NOTE_PATH + "/" + id
////        val response = client.delete {
////            url(url)
//////            parameter("id", id)
////        }
////        when (response.status) {
////            HttpStatusCode.OK -> {
////                val responseText = response.bodyAsText()
//////                val jsonObject = Json.decodeFromString<Notes>(responseText)
////                return Result(success = true)
////            }
////            else -> {
////                val responseText = response.bodyAsText()
////                val jsonObject = Json.decodeFromString<Error>(responseText)
////                return Result(success = false)
////            }
////        }
//    }
//
    override suspend fun getNote(id: Int) : Note? {
        return localDataSource.getNote(id)
    }
}