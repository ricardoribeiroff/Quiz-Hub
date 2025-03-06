package dev.app.quizhub.ui.createQuestionsAI

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import dev.app.quizhub.data.ApiService
import dev.app.quizhub.ui.theme.QuizhubTheme
import kotlinx.coroutines.launch

@Composable
fun CreateQuestionsAIScreen(
    navController: NavController,
    createQuestionsAIViewModel: CreateQuestionsAIViewModel = viewModel()
){
    val state = createQuestionsAIViewModel.state
    val coroutineScope = rememberCoroutineScope()
    val apiService = ApiService()

    QuizhubTheme {
        Scaffold(
            topBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Quiz \nHub",
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        modifier = Modifier.padding(top = 140.dp),
                        text = "Gemini",
                        style = MaterialTheme.typography.displaySmall,
                        fontSize = 20.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            },
            bottomBar = {
                BottomAppBar(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    actions = {
                        IconButton(onClick = { navController.navigate("HomeScreen") }) {
                            Icon(Icons.Filled.Home, contentDescription = "Home")
                        }
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = { createQuestionsAIViewModel.getQuestions() },
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ) {
                            Icon(Icons.Filled.Add, contentDescription = "Add")
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp)
            ) {
                Text(
                    text = "Tema das Questões",
                    style = MaterialTheme.typography.titleLarge,
                )
                TextField(
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.onPrimary,
                        unfocusedContainerColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    value = state.questionsTheme,
                    onValueChange = { createQuestionsAIViewModel.onQuestionsThemeChange(it) },
                )
                Text(
                    text = "Dificuldade das Questões",
                    style = MaterialTheme.typography.titleLarge,
                )
                TextField(
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.onPrimary,
                        unfocusedContainerColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    value = state.questionsDifficulty,
                    onValueChange = {createQuestionsAIViewModel.onQuestionsDifficultyChange(it)}
                )
                Text(
                text = "Quantidade de Questões",
                style = MaterialTheme.typography.titleLarge,
                )
                TextField(
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.onPrimary,
                        unfocusedContainerColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    value = state.questionsAmount,
                    onValueChange = {createQuestionsAIViewModel.onQuestionsAmountChange(it)}
                )
            }
        }
    }
}