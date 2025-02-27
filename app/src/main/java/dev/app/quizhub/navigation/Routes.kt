package dev.app.quizhub.navigation

sealed class Screen(val route: String) {
    object Home : Screen("HomeScreen")
    object CreateCollection : Screen("CreateCollectionScreen")

}