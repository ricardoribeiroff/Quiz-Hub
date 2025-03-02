// File: app/src/main/java/dev/app/quizhub/ui/questions/QuestionsScreen.kt
package dev.app.quizhub.ui.questions

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import dev.app.quizhub.ui.theme.QuizhubTheme
import kotlinx.coroutines.launch
import androidx.compose.foundation.background

@Composable
fun QuestionsScreen(
    navController: NavController,
    setId: String,
    questionsViewModel: QuestionsViewModel = viewModel(),
) {
    val questions = questionsViewModel.question.collectAsState(initial = emptyList())
    val alternatives = questionsViewModel.alternative.collectAsState(initial = emptyList())
    val selectedAlternatives = questionsViewModel.selectedAlternatives.collectAsState(initial = emptyMap())
    val evaluationResult = questionsViewModel.evaluationResult.collectAsState(initial = emptyMap())
    val isEvaluated = questionsViewModel.isEvaluated.collectAsState(initial = false)
    val questionSet = questionsViewModel.questionSet.collectAsState(initial = null)
    val isReadOnly = questionsViewModel.isReadOnly.collectAsState(initial = false)
    val coroutineScope = rememberCoroutineScope()
    val questionNumbers = remember {
        mutableStateOf(List(100) { it + 1 })
    }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            questionsViewModel.fetchQuestions(setId)
            questionsViewModel.fetchAlternatives()
        }
    }

    QuizhubTheme {
        Scaffold(
            topBar = {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(20.dp)
                ) {
                    Text(
                        text = "Quiz \nHub",
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        modifier = Modifier.padding(top = 140.dp),
                        text = questionSet.value?.name ?: "",
                        style = MaterialTheme.typography.headlineMedium,
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
                        if (!isReadOnly.value) {
                            FloatingActionButton(
                                containerColor = MaterialTheme.colorScheme.onPrimary,
                                onClick = {
                                    questionsViewModel.evaluateAndNavigate { success ->
                                        if (success) {
                                            navController.popBackStack()
                                        }
                                    }
                                },
                                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                            ) {
                                Icon(Icons.Filled.Check, "Evaluate Answers")
                            }
                        }
                    }
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .offset(y = -20.dp)
            ) {
                HorizontalDivider()
                Spacer(modifier = Modifier.padding(8.dp))
                questions.value.forEachIndexed { index, question ->
                    val selectedAlternativeId = selectedAlternatives.value[question.id]
                    Text(
                        text = "${questionNumbers.value[index]} -  ${question.questionText}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    val alternativesForQuestion = alternatives.value.filter { it.questionId == question.id }
                    alternativesForQuestion.forEachIndexed { altIndex, alternative ->
                        val marker = ('a' + altIndex)
                        val isSelected = selectedAlternativeId == alternative.id
                        val isFinishedAndCorrect = alternative.isFinished && alternative.isCorrect
                        val isFinishedAndWrong = alternative.isFinished && !alternative.isCorrect

                        AssistChip(
                            onClick = {
                                if (!isReadOnly.value) {
                                    questionsViewModel.toggleAlternativeSelection(question.id, alternative.id)
                                }
                            },
                            label = { Text("$marker) ${alternative.alternativeText}") },
                            border = BorderStroke(
                                width = -1.dp,
                                color = when {
                                    isFinishedAndCorrect -> Color(0xFFB9F6CA)
                                    isFinishedAndWrong -> MaterialTheme.colorScheme.error
                                    isSelected -> MaterialTheme.colorScheme.primary
                                    else -> MaterialTheme.colorScheme.outline
                                }
                            ),
                            shape = MaterialTheme.shapes.extraSmall,
                            colors = AssistChipDefaults.assistChipColors(
                                containerColor = when {
                                    isFinishedAndCorrect -> Color(0xFFB9F6CA)
                                    isFinishedAndWrong -> MaterialTheme.colorScheme.errorContainer
                                    isSelected -> MaterialTheme.colorScheme.primaryContainer
                                    else -> Color(0x00FFFFFF)
                                },
                                labelColor = when {
                                    isFinishedAndCorrect -> MaterialTheme.colorScheme.onTertiaryContainer
                                    isFinishedAndWrong -> MaterialTheme.colorScheme.onErrorContainer
                                    isSelected -> MaterialTheme.colorScheme.onPrimaryContainer
                                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                                },
                                disabledContainerColor = when {
                                    isFinishedAndCorrect -> Color(0xFFB9F6CA)
                                    isFinishedAndWrong -> MaterialTheme.colorScheme.errorContainer
                                    isSelected -> MaterialTheme.colorScheme.primaryContainer
                                    else -> Color(0x00FFFFFF)
                                },
                                disabledLabelColor = when {
                                    isFinishedAndCorrect -> MaterialTheme.colorScheme.onTertiaryContainer
                                    isFinishedAndWrong -> MaterialTheme.colorScheme.onErrorContainer
                                    isSelected -> MaterialTheme.colorScheme.onPrimaryContainer
                                    else -> MaterialTheme.colorScheme.onSurfaceVariant
                                }
                            ),
                            enabled = !isReadOnly.value
                        )
                    }
                    HorizontalDivider()
                    if (isEvaluated.value) {
                        Text(
                            text = question.explanation ?: "",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }
                    Spacer(modifier = Modifier.padding(8.dp))
                }
            }
        }
    }
}