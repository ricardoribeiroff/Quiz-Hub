package dev.app.quizhub.model

import kotlinx.serialization.Serializable

@Serializable
data class QuestionSet(
    val id: Int = 0,
    val sectionId: String = "",
    val name: String = "",
    val description: String = "",
)