package dev.app.quizhub.ui.createCollection

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import dev.app.quizhub.data.CollectionDAO
import dev.app.quizhub.ui.home.HomeViewModel
import dev.app.quizhub.ui.theme.QuizhubTheme

@Composable
fun CreateCollection(
    navController: NavController,
    homeViewModel: HomeViewModel = viewModel(),
    createCollectionViewModel: CreateCollectionViewModel = viewModel()
) {
    val state = createCollectionViewModel.state
    val dao = CollectionDAO(context = navController.context)
    QuizhubTheme() {
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
                        IconButton(onClick = {}) {
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
                            Icon(Icons.Filled.Add, "Localized description")
                        }
                    }
                ) }
        ) { innerPadding ->
            Column(modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 20.dp)) {
                Text(
                    text = "Nome da Coleção",
                    style = MaterialTheme.typography.titleLarge)
                TextField(
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.onPrimary,
                        unfocusedContainerColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                    value = state.name,
                    onValueChange = {createCollectionViewModel.onNameChange(it)},
                )
                Text(
                    text = "Descrição",
                    style = MaterialTheme.typography.titleLarge)
                TextField(
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.onPrimary,
                        unfocusedContainerColor = MaterialTheme.colorScheme.onPrimary,
                    ),
                    value = state.description,
                    onValueChange = {createCollectionViewModel.onDescriptionChange(it)},
                )
            }
        }
    }
}
