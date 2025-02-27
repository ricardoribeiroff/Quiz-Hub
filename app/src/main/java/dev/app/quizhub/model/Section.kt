package dev.app.quizhub.model

data class Section(
    val id: Long = 0,
    val collectionId: Long,
    val name: String,
    val description: String? = null,
    val orderIndex: Int = 0,
    val createdAt: String? = null,
    val updatedAt: String? = null
)