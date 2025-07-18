package com.joshayoung.notemark.data

import com.joshayoung.notemark.BuildConfig
import com.joshayoung.notemark.domain.NoteMarkRepository
import com.joshayoung.notemark.domain.Registration
import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpStatusCode
import kotlinx.serialization.json.Json

typealias Result = com.joshayoung.notemark.domain.Result

class NoteMarkRepositoryImpl (
    private val client: HttpClient,
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
}