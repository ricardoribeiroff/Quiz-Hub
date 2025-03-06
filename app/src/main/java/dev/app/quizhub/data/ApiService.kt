package dev.app.quizhub.data

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import dev.app.quizhub.model.AIQuery
import io.ktor.client.*
import io.ktor.client.request.headers
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.append
import kotlinx.serialization.json.Json

class ApiService {

    var state by mutableStateOf(AIQuery())
        private set
    
    suspend fun getQuestions(state: AIQuery) {
        val client = HttpClient()
        try {
            val requestBody = mapOf(
                "contents" to listOf(
                    mapOf(
                        "parts" to listOf(
                            mapOf("text" to "Me fale sobre ${state.questionsTheme}")
                        )
                    )
                )
            )

            val response: HttpResponse = client.post("https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key=AIzaSyCH7Mh8_xRK71pC776sj1XWbmQIbWUeE2g") {
                headers {
                    append(HttpHeaders.ContentType, ContentType.Application.Json)
                }
                setBody(Json.encodeToString(requestBody))
            }
            Log.d("APIRESPONSE", response.bodyAsText().toString())
        } catch (e: Exception) {
            Log.e("APISERVICE", "Erro na requisição: ${e.message}", e)
            throw e
        }
    }
}
