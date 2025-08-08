package com.joshayoung.notemark.note.data.network

import com.joshayoung.notemark.BuildConfig
import com.joshayoung.notemark.core.data.networking.catchErrors
import com.joshayoung.notemark.core.domain.util.DataError
import com.joshayoung.notemark.core.domain.util.Result
import com.joshayoung.notemark.core.domain.util.map
import com.joshayoung.notemark.note.data.mappers.toNoteDto
import com.joshayoung.notemark.note.domain.models.Note
import com.joshayoung.notemark.note.network.NoteDto
import com.joshayoung.notemark.note.network.RemoteDataSource
import com.joshayoung.notemark.note.network.toNote
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url


class KtorRemoteDataSource(
    private val client: HttpClient,
) : RemoteDataSource {
    override suspend fun saveNote(note: Note): Result<Note, DataError.Network> {
        val noteDto = note.toNoteDto()
        val response = catchErrors<NoteDto> {
            client.post {
                url(BuildConfig.BASE_URL + BuildConfig.NOTE_PATH)
                setBody(noteDto)
            }
        }

        return response.map {
            it.toNote()
        }
    }

    override suspend fun getNotes(): Result<List<Note>, DataError.Network> {
        val response = catchErrors<List<NoteDto>> {
            client.get {
                url(BuildConfig.BASE_URL + BuildConfig.NOTE_PATH)
            }
        }

        return response.map { noteDto ->
            noteDto.map { it.toNote() }
        }
    }
}