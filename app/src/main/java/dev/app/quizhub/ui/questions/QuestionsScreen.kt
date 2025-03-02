// File: app/src/main/java/dev/app/quizhub/ui/questions/QuestionsScreen.kt
package dev.app.quizhub.ui.questions

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
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
    val questions = questionsViewModel.question.collectAsState(initial = emptyList())
    val alternatives = questionsViewModel.alternative.collectAsState(initial = emptyList())
    val coroutineScope = rememberCoroutineScope()
    val questionNumbers = remember {
        mutableStateOf(List(100) { it + 1 })
    }
    // Mapping of question id to alternative id for selection
    var selectedAlternatives = remember { mutableStateOf(mutableMapOf<Long, Long>()) }

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            questionsViewModel.fetchQuestions(setId)
            questionsViewModel.fetchAlternatives()
            Log.d("DEBUGLOG", "ALTERNATIVES: ${alternatives.value}")
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
                        text = "Quiz \\nHub",
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
                        IconButton(onClick = { navController.navigate("HomeScreen") }) {
                            Icon(Icons.Filled.Home, contentDescription = "Home")
                        }
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            containerColor = MaterialTheme.colorScheme.onPrimary,
                            onClick = {
                                // Ensure all questions have an alternative selected
                                if (selectedAlternatives.value.size < questions.value.size) {
                                    Log.d("EVALUATION", "Please answer all questions before evaluation.")
                                    return@FloatingActionButton
                                }
                                // Evaluate all selected alternatives
                                selectedAlternatives.value.forEach { (questionId, alternativeId) ->
                                    val chosenAlternative = alternatives.value.firstOrNull { it.id == alternativeId }
                                    if (chosenAlternative != null) {
                                        Log.d(
                                            "EVALUATION",
                                            "Question id $questionId: Alternative id $alternativeId isCorrect = ${chosenAlternative.isCorrect}"
                                        )
                                    } else {
                                        Log.d("EVALUATION", "Question id $questionId: No alternative found")
                                    }
                                }
                            },
                            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                        ) {
                            Icon(Icons.Filled.Check, "Evaluate Answers")
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
                        AssistChip(
                            onClick = {
                                selectedAlternatives.value = selectedAlternatives.value.toMutableMap().apply {
                                    if (isSelected) remove(question.id) else put(question.id, alternative.id)
                                }
                                Log.d(
                                    "DEBUGLOG",
                                    "Question id ${question.id} selected alternative id: ${alternative.id}"
                                )
                            },
                            label = { Text("$marker) ${alternative.alternativeText}") },
                            border = BorderStroke(
                                width = -1.dp,
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
                    }
                    HorizontalDivider()
                    Spacer(modifier = Modifier.padding(8.dp))
                }
            }
        }
    }
}