package dev.app.quizhub.ui.login

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import dev.app.quizhub.data.DatabaseHelper
import dev.app.quizhub.model.LoginState
import io.github.jan.supabase.auth.auth
import io.github.jan.supabase.auth.providers.builtin.Email
import kotlinx.coroutines.launch


class LoginViewModel(

) : ViewModel() {
    val supabase = DatabaseHelper().supabase

    var state by mutableStateOf(LoginState())

    fun onEmailChange(email: String) {
        state = state.copy(email = email)
    }

    fun onPasswordChange(password: String) {
        state = state.copy(password = password)
    }

    fun onTogglePasswordVisibility() {
        state = state.copy(isPasswordVisible = !state.isPasswordVisible)
    }

    suspend fun onLogin(navController: NavController) {
            try {
                supabase.auth.signInWith(Email) {
                    email = state.email
                    password = state.password
                }
                navController.navigate("HomeScreen")
                Log.d("LOGIN STATUS", "Login efetuado com sucesso")
            } catch (e: Exception) {
                Log.e("LOGIN STATUS", "Login falhou", e)
            }

    }



}