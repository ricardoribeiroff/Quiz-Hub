package dev.app.quizhub.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.io.Serial

@Serializable
data class QuestionSet(
    @SerialName("id")
    val id: Int = 0,
    @SerialName("section_id")
    val sectionId: Int = 0,
    @SerialName("name")
    val name: String = "",
    @SerialName("description")
    val description: String = "",
    @SerialName("is_finished")
    val isFinished: Boolean = false,
)