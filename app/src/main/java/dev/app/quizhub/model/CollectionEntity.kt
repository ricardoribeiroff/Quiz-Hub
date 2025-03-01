package dev.app.quizhub.model

import kotlinx.serialization.Serializable

@Serializable
data class CollectionEntity(
    val id: Int = 0,
    val name: String = "",
    val description: String = "",
    val owner: String = "",
    var uidUser: String = "",
    val created_at: String = "",
    val updated_at: String = ""
)