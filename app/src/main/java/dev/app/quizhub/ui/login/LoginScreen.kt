import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import dev.app.quizhub.ui.login.LoginViewModel
import dev.app.quizhub.ui.theme.QuizhubTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    loginViewModel: LoginViewModel = viewModel()
) {
    val state = loginViewModel.state
    val couroutineScope = rememberCoroutineScope()
    QuizhubTheme {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = state.email,
                onValueChange = { loginViewModel.onEmailChange(it) },
                label = { Text("Email") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedContainerColor = MaterialTheme.colorScheme.onPrimary,
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Next
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = state.password,
                onValueChange = { loginViewModel.onPasswordChange(it) },
                label = { Text("Senha") },
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedContainerColor = MaterialTheme.colorScheme.onPrimary,
                ),
                visualTransformation = if (state.isPasswordVisible) {
                    VisualTransformation.None
                } else {
                    PasswordVisualTransformation()
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Send
                ),
                trailingIcon = {
                    IconButton(onClick = { loginViewModel.onTogglePasswordVisibility() }) {
                        Icon(
                            imageVector = Icons.Filled.Lock,
                            contentDescription = if (state.isPasswordVisible)
                                "Ocultar senha"
                            else
                                "Mostrar senha"
                        )
                    }
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    couroutineScope.launch {
                        loginViewModel.onLogin(navController)
                    }
                    Log.d("Login Button", "LOGIN CLICADO")
                },
                colors = ButtonColors(
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.colorScheme.onPrimary,
                    MaterialTheme.colorScheme.primary,
                    MaterialTheme.colorScheme.onPrimary),
                modifier = Modifier
                    .width(250.dp)
            ) {
                Text("Login", fontSize = 18.sp)
            }
            TextButton(
                modifier = Modifier
                    .offset(y = (-10).dp),
                onClick = {
                    TODO()
                }
            ){
                Text("Esqueceu a senha?")
            }
            Button(
                modifier = Modifier
                    .offset(y = (-20).dp)
                    .width(250.dp),
                onClick = {
                    TODO()
                }
            ) {
                Text("Cadastro", fontSize = 18.sp)
            }
        }

    }


}