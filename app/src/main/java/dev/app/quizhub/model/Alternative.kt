package dev.app.quizhub.model

data class Alternative(
    val id: Long = 0,
    val questionId: Long,
    val alternativeText: String,
    val isCorrect: Boolean = false,
    val orderIndex: Int = 0,
    val createdAt: String? = null,
    val updatedAt: String? = null
)