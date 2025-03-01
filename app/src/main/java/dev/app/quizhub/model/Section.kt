package dev.app.quizhub.model


import kotlinx.serialization.Serializable

@Serializable
data class Section(
    val id: Int = 0,
    val collectionId: String = "",
    val name: String = "",
    val description: String = ""
)