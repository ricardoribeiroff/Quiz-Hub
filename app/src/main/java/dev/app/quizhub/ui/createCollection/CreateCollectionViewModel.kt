package dev.app.quizhub.ui.createCollection

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dev.app.quizhub.data.CollectionDAO
import dev.app.quizhub.model.CollectionEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreateCollectionViewModel(application: Application) : AndroidViewModel(application) {

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
            CollectionDAO().insert(state)
        }
    }
}