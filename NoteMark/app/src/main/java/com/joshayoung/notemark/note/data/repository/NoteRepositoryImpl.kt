package com.joshayoung.notemark.note.data.repository

import com.joshayoung.notemark.BuildConfig
import com.joshayoung.notemark.core.domain.models.Error
import com.joshayoung.notemark.core.domain.util.DataError
import com.joshayoung.notemark.core.domain.util.EmptyDataResult
import com.joshayoung.notemark.core.domain.util.EmptyResult
import com.joshayoung.notemark.core.domain.util.Result
import com.joshayoung.notemark.core.domain.util.asEmptyDataResult
import com.joshayoung.notemark.core.utils.getTimeStampForInsert
import com.joshayoung.notemark.note.data.database.entity.NoteEntity
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
import java.util.UUID


class NoteRepositoryImpl (
    private val client: HttpClient,
    private val localDataSource: LocalDataSource,
    private val applicationScope: CoroutineScope,
    private val remoteDataSource: KtorRemoteDataSource
) : NoteRepository {
    override suspend fun createNote(title: String, body: String): EmptyDataResult<DataError> {
        val remoteId = UUID.randomUUID().toString()
        val note = Note(
            remoteId = remoteId,
            title = title,
            content = body,
            createdAt = getTimeStampForInsert()
        )
        val localResult = localDataSource.upsertNote(note)

        // TODO: Is this right?
        if (localResult is Result.Success)
        {
            applicationScope.async {
                val noteSave = remoteDataSource.saveNote(note)
                if (noteSave is Result.Success)
                {

                    // left off: Update local note with remote ID


                    return@async Result.Success(Unit)
                }
            }.await()
        }

        return localResult.asEmptyDataResult()
    }

    override suspend fun updateNote(note: Note?, title: String, body: String): EmptyResult<DataError> {
        val localNoteForUpdate = Note(
            id = note?.id,
            title = title,
            content = body,
            createdAt = note!!.createdAt,
            remoteId = note.remoteId
        )
        val result = localDataSource.upsertNote(localNoteForUpdate)

        if (result is Result.Success)
        {
            applicationScope.async {
                val remoteUpdate = remoteDataSource.updateNote(localNoteForUpdate)
                if (remoteUpdate is Result.Success) {

                    return@async Result.Success(Unit)
                }
            }.await()

            return Result.Success(Unit)
        }

        return result.asEmptyDataResult();
    }

    override suspend fun getNotes(): Flow<List<Note>> {
        return localDataSource.getNotes()
    }

    override suspend fun deleteNote(id: Int) {
        localDataSource.deleteNote(id)
    }

    override suspend fun getNote(id: Int) : Note? {
        return localDataSource.getNote(id)
    }
}