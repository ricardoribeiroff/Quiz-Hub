package dev.app.quizhub.model

data class Question(
    val id: Long = 0,
    val setId: Long,
    val questionText: String,
    val explanation: String? = null,
    val orderIndex: Int = 0,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val alternatives: List<Alternative> = emptyList()
)
