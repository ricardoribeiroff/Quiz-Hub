package dev.app.quizhub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.app.quizhub.navigation.Screen
import dev.app.quizhub.ui.createCollection.CreateCollectionScreen
import dev.app.quizhub.ui.createSections.CreateSectionsScreen
import dev.app.quizhub.ui.home.HomeScreen
import dev.app.quizhub.ui.sections.QuestionSetsScreen
import dev.app.quizhub.ui.sections.SectionsScreen
import dev.app.quizhub.ui.shared.SharedViewModel

class MainActivity : ComponentActivity() {
    private val sharedViewModel: SharedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route
            ) {
                composable(Screen.Home.route) {
                    HomeScreen(navController, sharedViewModel)
                }
                composable(Screen.CreateCollection.route) {
                    CreateCollectionScreen(navController)
                }
                composable(Screen.CreateSection.route) {
                    CreateSectionsScreen(navController, sharedViewModel)
                }
                composable(Screen.Sections.route) {
                    SectionsScreen(navController, sharedViewModel.collectionId.value ?: "", sharedViewModel)
                }
                composable(Screen.QuestionSets.route) {
                    QuestionSetsScreen(navController, sharedViewModel.sectionId.value ?: "")
                }
            }
        }

        sharedViewModel.collectionId.observe(this, Observer { id ->
            // Atualize a variável id aqui
            val currentCollectionId = id
        })
    }
}