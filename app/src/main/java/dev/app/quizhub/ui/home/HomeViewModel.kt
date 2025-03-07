package dev.app.quizhub.ui.home

import android.app.Application
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import dev.app.quizhub.data.CollectionDAO
import dev.app.quizhub.data.DatabaseHelper
import dev.app.quizhub.model.CollectionEntity
import dev.app.quizhub.model.HomeState
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val _collections = MutableStateFlow<List<CollectionEntity>>(emptyList())
    val collections: StateFlow<List<CollectionEntity>> = _collections
    val supabase = DatabaseHelper().supabase

    var state by mutableStateOf(HomeState())
        private set

    fun onCollectionSelected(collectionId: String) {
        state = state.copy(collectionId = collectionId)
    }

    fun fetchCollections() {
        viewModelScope.launch {
            _collections.value = CollectionDAO().getByUserUid(
                uidUser = supabase.auth.retrieveUserForCurrentSession(updateSession = true).id
            )
        }
    }

    fun navigateToSections(navController: NavController, collectionId: String) {
        navController.navigate("SectionsScreen/$collectionId")
    }
}