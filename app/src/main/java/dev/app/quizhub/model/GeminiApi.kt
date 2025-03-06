package dev.app.quizhub.model

import kotlinx.serialization.Serializable

@Serializable
data class GeminiApi (
    var response: String = ""
)