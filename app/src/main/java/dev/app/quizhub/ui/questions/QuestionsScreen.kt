package dev.app.quizhub.ui.questions

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import dev.app.quizhub.ui.theme.QuizhubTheme
import kotlinx.coroutines.launch

@Composable
fun QuestionsScreen(
    navController: NavController,
    setId: String,
    questionsViewModel: QuestionsViewModel = viewModel(),
) {
    var isAlternativeSelected by remember { mutableStateOf(false) }
    val questions = questionsViewModel.question.collectAsState(initial = emptyList())
    val coroutineScope = rememberCoroutineScope()
    var questionNumbers = remember {
        mutableStateOf(List(100) { it + 1 })  // Pre-generate numbers 1-100
    }
    var selectedQuestions = remember { mutableStateOf(mutableMapOf<Long, Boolean>()) }


    LaunchedEffect(Unit) {
        coroutineScope.launch {
            questionsViewModel.fetchQuestions(setId)
            questionsViewModel.fetchAlternatives("1")
        }
    }

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
                        text = "Atividade 1",
                        style = MaterialTheme.typography.headlineMedium,
                        fontSize = 24.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            },
            bottomBar = {
                BottomAppBar(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    actions = {
                        IconButton(onClick = {navController.navigate("HomeScreen")}) {
                            Icon(Icons.Filled.Home, contentDescription = "Home")
                        }
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            containerColor = MaterialTheme.colorScheme.onPrimary,
                            onClick = { navController.navigate("CreateQuestionSetScreen") },
                            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                        ) {
                            Icon(Icons.Filled.Add, "Add Question Set")
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .offset(y = -20.dp)
            ) {
                HorizontalDivider()
                Spacer(modifier = Modifier.padding(8.dp))
                questions.value.forEachIndexed() { index, questions ->
                    val isSelected = selectedQuestions.value[questions.id] ?: false
                    Text(
                        text = "${questionNumbers.value[index]} -  ${questions.questionText}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    AssistChip(
                        onClick = {
                            selectedQuestions.value = selectedQuestions.value.toMutableMap().apply {
                                this[questions.id] = !isSelected }
                        },
                        label = { Text("a) ListView")},
                        border = BorderStroke(
                            width = if (isSelected) -1.dp else -1.dp,
                            color = if (isSelected)
                                MaterialTheme.colorScheme.primary
                            else
                                MaterialTheme.colorScheme.outline
                        ),
                        shape = MaterialTheme.shapes.extraSmall,
                        colors = AssistChipDefaults.assistChipColors(
                            containerColor = if (isSelected)
                                MaterialTheme.colorScheme.primaryContainer
                            else
                                AssistChipDefaults.assistChipColors().containerColor,
                            labelColor = if (isSelected)
                                MaterialTheme.colorScheme.onPrimaryContainer
                            else
                                AssistChipDefaults.assistChipColors().labelColor
                        )
                    )
                    Spacer(modifier = Modifier.padding(8.dp))
                    HorizontalDivider()
                }

            }
        }
    }
}