package dev.app.quizhub.ui.createQuestionSet

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import dev.app.quizhub.ui.shared.SharedViewModel
import dev.app.quizhub.ui.theme.QuizhubTheme

@Composable
fun CreateQuestionSetScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel,
    createQuestionSetViewModel: CreateQuestionSetViewModel = viewModel()
) {
    val state = createQuestionSetViewModel.state
    val sectionId by sharedViewModel.sectionId.observeAsState("")
    LaunchedEffect(sectionId) {
        createQuestionSetViewModel.setSectionId(sectionId)
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
                        text = "Criar Set de Questões",
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
                            containerColor = MaterialTheme.colorScheme.onPrimary,
                            onClick = {
                                createQuestionSetViewModel.saveQuestionSet()
                                navController.popBackStack()
                            },
                            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                        ) {
                            Icon(Icons.Filled.Add, contentDescription = "Save Question Set")
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
                    text = "Nome do Set",
                    style = MaterialTheme.typography.titleLarge
                )
                TextField(
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.onPrimary,
                        unfocusedContainerColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    value = state.name,
                    onValueChange = { createQuestionSetViewModel.onNameChange(it) }
                )
                Text(
                    text = "Descrição",
                    style = MaterialTheme.typography.titleLarge
                )
                TextField(
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.onPrimary,
                        unfocusedContainerColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    value = state.description,
                    onValueChange = { createQuestionSetViewModel.onDescriptionChange(it) }
                )
            }
        }
    }
}