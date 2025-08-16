package com.joshayoung.notemark.note.data.repository

import com.joshayoung.notemark.core.domain.util.DataError
import com.joshayoung.notemark.core.domain.util.EmptyResult
import com.joshayoung.notemark.core.domain.util.Result
import com.joshayoung.notemark.core.utils.getTimeStampForInsert
import com.joshayoung.notemark.note.data.database.entity.SyncOperation
import com.joshayoung.notemark.note.data.mappers.toNote
import com.joshayoung.notemark.note.domain.database.LocalDataSource
import com.joshayoung.notemark.note.domain.database.LocalSyncDataSource
import com.joshayoung.notemark.note.domain.models.Note
import com.joshayoung.notemark.note.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.UUID
import kotlin.time.ExperimentalTime


@OptIn(ExperimentalTime::class)
class NoteRepositoryImpl (
    private val localDataSource: LocalDataSource,
    private val localSyncDataSource: LocalSyncDataSource
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

        if (localResult is Result.Success) {
            // Add to Sync Queue
//            localSyncDataSource.addOrUpdateQueue(note, SyncOperation.CREATE)

            return Result.Success(data = localResult.data.toNote())
        } else {
            // NOTE: This might not be correct:
            return Result.Error(error = DataError.Local.DISK_FULL)
        }
    }

    override suspend fun updateNote(note: Note?, title: String, body: String): Result<Note, DataError> {
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
//            val sync = localSyncDataSource.getSync(localNoteForUpdate)
//            localSyncDataSource.addOrUpdateQueue(localNoteForUpdate, SyncOperation.UPDATE)

            return Result.Success(localNoteForUpdate)
        }

        // TODO: Is this right?
        return Result.Error(error = DataError.Network.UNKNOWN)
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
            localSyncDataSource.addOrUpdateQueue(note, SyncOperation.DELETE)
            return Result.Success(Unit)
        }

        return Result.Error(error = DataError.Network.UNKNOWN)
    }

    override suspend fun getNote(id: Long) : Note? {
        return localDataSource.getNote(id)
    }
}