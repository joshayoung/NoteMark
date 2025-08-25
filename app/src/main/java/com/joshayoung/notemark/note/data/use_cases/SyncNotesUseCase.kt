package com.joshayoung.notemark.note.data.use_cases

import com.joshayoung.notemark.app.DateHelper
import com.joshayoung.notemark.core.domain.util.Result
import com.joshayoung.notemark.note.data.database.entity.SyncOperation
import com.joshayoung.notemark.note.data.network.KtorRemoteDataSource
import com.joshayoung.notemark.note.domain.database.LocalSyncDataSource
import com.joshayoung.notemark.note.domain.models.Note
import kotlinx.coroutines.flow.first
import kotlinx.serialization.json.Json
import java.time.format.DateTimeFormatter

class SyncNotesUseCase(
    private val remoteDataSource: KtorRemoteDataSource,
    private val localSyncDataSource: LocalSyncDataSource,
) {
    suspend fun execute() {
        // listen to the getNotes flow instead of having to call this 3 times:
        var remoteNotes = remoteDataSource.getNotes()
        var serverNotes = emptyList<Note>()
        if (remoteNotes is Result.Success) {
            // Add this a different way:
            serverNotes = remoteNotes.data
        }
        val syncs = localSyncDataSource.getAllSyncs()
        val syncList = syncs.first()

        val createSyncs =
            syncList.filter { sync ->
                sync.operation == SyncOperation.CREATE
            }

        val deleteSyncs =
            syncList.filter { sync ->
                sync.operation == SyncOperation.DELETE
            }

        val updateSyncs =
            syncList.filter { sync ->
                sync.operation == SyncOperation.UPDATE
            }

        createSyncs.forEach { sync ->
            // if create and does note exist on server
            // create it
            val syncNote: Note = Json.decodeFromString(sync.payload)
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:ss:mm")
            val remoteVersion =
                serverNotes.find { remote ->
                    remote.remoteId == syncNote.remoteId
                }

            if (remoteVersion == null) {
                remoteDataSource.createNote(syncNote)
                // Should not ever happen
            } else {
                val remoteDate = DateHelper.convertToDate(remoteVersion.lastEditedAt)
                val localNote = DateHelper.convertToDate(syncNote.lastEditedAt)

                if (remoteDate?.isBefore(localNote) ?: false) {
                    remoteDataSource.deleteNote(remoteVersion.remoteId)
                    remoteDataSource.createNote(syncNote)
                }
            }

            // if after just delete the local sync
            localSyncDataSource.deleteSync(sync.id)
        }

        // listen to the remote flow instead of having to call this multiple times:
        remoteNotes = remoteDataSource.getNotes()
        serverNotes = emptyList<Note>()
        if (remoteNotes is Result.Success) {
            // Add this a different way:
            serverNotes = remoteNotes.data
        }

        deleteSyncs.forEach { sync ->
            // if delete and last updated is newer than server
            // remove from server
            // else remove sync item
            val syncNote: Note = Json.decodeFromString(sync.payload)
            val remoteVersion =
                serverNotes.find { remote ->
                    remote.remoteId == syncNote.remoteId
                }
            if (remoteVersion != null) {
                val remoteDate = DateHelper.convertToDate(remoteVersion.lastEditedAt)
                val localNote = DateHelper.convertToDate(syncNote.lastEditedAt)

                if (remoteDate?.isBefore(localNote) ?: false || remoteDate?.isEqual(localNote) ?: false) {
                    // TODO: Improve null check:
                    remoteDataSource.deleteNote(remoteVersion.remoteId!!)
                }
            }
            localSyncDataSource.deleteSync(sync.id)
        }

        // listen to the remote flow instead of having to call this multiple times:
        remoteNotes = remoteDataSource.getNotes()
        serverNotes = emptyList<Note>()
        if (remoteNotes is Result.Success) {
            // Add this a different way:
            serverNotes = remoteNotes.data
        }

        updateSyncs.forEach { sync ->
            // if update and does not exist on server
            // ignore, odd case that should not happen

            // if update and exists on server, newest timestamp wins (local or remote)
            val syncNote: Note = Json.decodeFromString(sync.payload)
            val remoteVersion =
                serverNotes.find { remote ->
                    remote.remoteId == syncNote.remoteId
                }

            // TODO: If update operation does not have a remote version, what do we do?
            if (remoteVersion == null) {
                // nothing?
            }

            if (remoteVersion != null) {
                val remoteDate = DateHelper.convertToDate(remoteVersion.lastEditedAt)
                val localNote = DateHelper.convertToDate(syncNote.lastEditedAt)

                if (remoteDate?.isBefore(localNote) ?: false) {
                    // mapper?
                    val note =
                        Note(
                            id = remoteVersion.id,
                            remoteId = remoteVersion.remoteId,
                            content = syncNote.content,
                            createdAt = remoteVersion.createdAt,
                            lastEditedAt = syncNote.lastEditedAt,
                            title = syncNote.title,
                        )
                    remoteDataSource.updateNote(note)
                }
            }
            localSyncDataSource.deleteSync(sync.id)
        }
        localSyncDataSource.clearSyncQueue()
    }
}