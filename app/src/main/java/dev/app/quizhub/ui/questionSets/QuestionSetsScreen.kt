package dev.app.quizhub.ui.sections

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import dev.app.quizhub.ui.theme.QuizhubTheme
import kotlinx.coroutines.launch

@Composable
fun QuestionSetsScreen(
    navController: NavController,
    sectionId: String,
    questionSetsViewModel: QuestionSetsViewModel = viewModel()
) {
    val sections = questionSetsViewModel.questionSet.collectAsState(initial = emptyList())
    val coroutineScope = rememberCoroutineScope()


    LaunchedEffect(Unit) {
        coroutineScope.launch {
            questionSetsViewModel.fetchSections(sectionId)
            Log.d("sectionId", "ID AQUI:${sectionId}")
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
                        text = "Question Sets",
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
                        IconButton(onClick = {navController.navigate("HomeScreen")}) {
                            Icon(Icons.Filled.Home, contentDescription = "Home")
                        }
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            containerColor = MaterialTheme.colorScheme.onPrimary,
                            onClick = { navController.navigate("CreateSectionsScreen") },
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
                sections.value.forEach { sections ->
                    ListItem(
                        headlineContent = { Text(sections.name) },
                        supportingContent = { Text(sections.description) },
                        leadingContent = {
                            Icon(
                                Icons.Filled.Star,
                                contentDescription = "Question Set icon",
                                modifier = Modifier.padding(top = 0.dp)
                            )
                        },
                        trailingContent = {  },
                        modifier = Modifier.clickable {
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}