package dev.app.quizhub.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Question(
    val id: Long = 0,
    @SerialName("set_id")
    val setId: Long = 0,
    @SerialName("question_text")
    val questionText: String = "",
    val explanation: String = "",
//    val alternatives: List<Alternative> = emptyList()
)
