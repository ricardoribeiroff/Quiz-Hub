package dev.app.quizhub.navigation

sealed class Screen(val route: String) {
    object Home : Screen("HomeScreen")
    object CreateCollection : Screen("CreateCollectionScreen")
    object CreateSection : Screen("CreateSectionsScreen")
    object Sections : Screen("SectionsScreen")
    object QuestionSets : Screen("QuestionSetsScreen")
}