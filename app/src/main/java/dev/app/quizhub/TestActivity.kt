package dev.app.quizhub

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.app.quizhub.navigation.Screen
import dev.app.quizhub.ui.createCollection.CreateCollection
import dev.app.quizhub.ui.home.HomeScreen

class TestActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController() // Cria o NavController
            NavHost(
                navController = navController,
                startDestination = Screen.Home.route // Define a tela inicial
            ) {
                composable(Screen.Home.route) { // Tela de login
                    HomeScreen(navController)
                }
                composable(Screen.CreateCollection.route) { // Tela de criação de collection
                    CreateCollection(navController)
                }
            }

        }
    }
}