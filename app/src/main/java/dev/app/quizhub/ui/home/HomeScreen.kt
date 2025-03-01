package dev.app.quizhub.ui.home

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
import dev.app.quizhub.data.DatabaseHelper
import dev.app.quizhub.ui.shared.SharedViewModel
import dev.app.quizhub.ui.theme.QuizhubTheme
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navController: NavController,
    sharedViewModel: SharedViewModel,
    homeViewModel: HomeViewModel = viewModel()
) {
    val collections = homeViewModel.collections.collectAsState(initial = emptyList())
    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        coroutineScope.launch {
            homeViewModel.fetchCollections()
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
                        text = "Coleções",
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
                            onClick = { navController.navigate("CreateCollectionScreen") },
                            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
                        ) {
                            Icon(Icons.Filled.Add, "Add collection")
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
                collections.value.forEach { collection ->
                    ListItem(
                        headlineContent = { Text(collection.name) },
                        supportingContent = { Text(collection.description) },
                        leadingContent = {
                            Icon(
                                Icons.Filled.Star,
                                contentDescription = "Collection icon",
                                modifier = Modifier.padding(top = 0.dp)
                            )
                        },
                        trailingContent = { Text(collection.owner) },
                        modifier = Modifier.clickable {
                            sharedViewModel.setCollectionId(collection.id.toString())
                            navController.navigate("SectionsScreen")
                        }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}