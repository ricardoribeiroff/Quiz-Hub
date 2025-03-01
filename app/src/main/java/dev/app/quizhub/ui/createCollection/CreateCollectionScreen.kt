package dev.app.quizhub.ui.createCollection

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import dev.app.quizhub.ui.theme.QuizhubTheme

@Composable
fun CreateCollectionScreen(
    navController: NavController,
    createCollectionViewModel: CreateCollectionViewModel = viewModel()
) {
    val state = createCollectionViewModel.state
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
                        text = "Criar Coleção",
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
                            onClick = {
                                createCollectionViewModel.saveCollection()
                                navController.popBackStack()
                            },
                            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                        ) {
                            Icon(Icons.Filled.Add, contentDescription = "Save Collection")
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
                    text = "Nome da Coleção",
                    style = MaterialTheme.typography.titleLarge
                )
                TextField(
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.onPrimary,
                        unfocusedContainerColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    value = state.name,
                    onValueChange = { createCollectionViewModel.onNameChange(it) }
                )
                Text(
                    text = "Criador",
                    style = MaterialTheme.typography.titleLarge
                )
                TextField(
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.onPrimary,
                        unfocusedContainerColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    value = state.owner,
                    onValueChange = { createCollectionViewModel.onOwnerChange(it) }
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
                    onValueChange = { createCollectionViewModel.onDescriptionChange(it) }
                )
            }
        }
    }
}