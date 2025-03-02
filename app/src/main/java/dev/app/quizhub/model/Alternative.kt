package dev.app.quizhub.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Alternative(
    val id: Long = 0,
    @SerialName("question_id")
    val questionId: Long = 0,
    @SerialName("alternative_text")
    val alternativeText: String = "",
    @SerialName("is_correct")
    val isCorrect: Boolean = false,
)