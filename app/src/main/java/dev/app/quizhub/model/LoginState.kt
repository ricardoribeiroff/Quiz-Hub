package dev.app.quizhub.model

data class LoginState(
    var email: String = "",
    var password: String = "",
    val isPasswordVisible : Boolean = false,
)