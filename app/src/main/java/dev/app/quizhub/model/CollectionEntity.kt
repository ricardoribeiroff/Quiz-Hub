package dev.app.quizhub.model

import com.google.firebase.Timestamp

data class CollectionEntity(
    val name: String = "",
    val description: String = "",
    val owner: String = "",
    val createdAt: Timestamp? = null,
    val updatedAt: Timestamp? = null
)