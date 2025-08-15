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
import com.joshayoung.notemark.note.data.mappers.toNote
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
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.UUID
import kotlin.time.Clock
import kotlin.time.ExperimentalTime
import kotlin.time.Instant


@OptIn(ExperimentalTime::class)
class NoteRepositoryImpl (
    private val localDataSource: LocalDataSource,
    private val applicationScope: CoroutineScope,
    private val remoteDataSource: KtorRemoteDataSource
) : NoteRepository {
    override suspend fun createNote(title: String, body: String): Result<Note, DataError> {
        val remoteId = UUID.randomUUID().toString()
        val note = Note(
            remoteId = remoteId,
            title = title,
            content = body,
            createdAt = getTimeStampForInsert()
        )
        val localResult = localDataSource.upsertNote(note)

        // TODO: Is this right?
//        if (localResult is Result.Success)
//        {
//            applicationScope.async {
//                val noteSave = remoteDataSource.saveNote(note)
//                if (noteSave is Result.Success)
//                {
//                    return@async Result.Success(Unit)
//                }
//            }.await()
//        }

        if (localResult is Result.Success) {
            return Result.Success(data = localResult.data.toNote())
        } else {
            // NOTE: This might not be correct:
            return Result.Error(error = DataError.Local.DISK_FULL)
        }
    }

    override suspend fun updateNote(note: Note?, title: String, body: String): EmptyResult<DataError> {
        // TODO: Move these to mapper:
        val currentDateTime = LocalDateTime.now(ZoneOffset.UTC)
        val currentInstant = currentDateTime.toInstant(ZoneOffset.UTC)
        val localNoteForUpdate = Note(
            id = note?.id,
            title = title,
            content = body,
            createdAt = note!!.createdAt,
            lastEditedAt = DateTimeFormatter.ISO_INSTANT.format(currentInstant),
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

    override suspend fun deleteNote(note: Note) : EmptyResult<DataError> {
        if (note.id == null)
        {
            return Result.Error(error = DataError.Network.UNKNOWN)
        }

        val result = localDataSource.deleteNote(note.id)
        if (result) {
            applicationScope.async {
                val remoteDelete = remoteDataSource.deleteNote(note.remoteId)
                if (remoteDelete is Result.Success) {
                    return@async Result.Success(Unit)
                }
            }.await()
        }

        return Result.Success(Unit)
    }

    override suspend fun getNote(id: Long) : Note? {
        return localDataSource.getNote(id)
    }
}