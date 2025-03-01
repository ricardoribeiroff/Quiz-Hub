package dev.app.quizhub.ui.createCollection

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dev.app.quizhub.data.CollectionDAO
import dev.app.quizhub.data.DatabaseHelper
import dev.app.quizhub.model.CollectionEntity
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreateCollectionViewModel(application: Application) : AndroidViewModel(application) {
    val supabase = DatabaseHelper().supabase
    var state by mutableStateOf(CollectionEntity())
        private set

    fun onNameChange(name: String) {
        state = state.copy(name = name)
    }

    fun onDescriptionChange(description: String) {
        state = state.copy(description = description)
    }
    fun onOwnerChange(owner: String) {
        state = state.copy(owner = owner)
    }

    fun saveCollection() {
        viewModelScope.launch(Dispatchers.IO) {
            state.uidUser = supabase.auth.retrieveUserForCurrentSession(updateSession = true).id
            CollectionDAO().insert(state)
        }
    }
}