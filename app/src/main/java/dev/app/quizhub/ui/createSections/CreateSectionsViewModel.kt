package dev.app.quizhub.ui.createSections

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dev.app.quizhub.data.SectionDAO
import dev.app.quizhub.model.Section
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class CreateSectionsViewModel(application: Application) : AndroidViewModel(application) {

    var state by mutableStateOf(Section())
        private set

    private val _sections = MutableStateFlow<List<Section>>(emptyList())
    val sections: MutableStateFlow<List<Section>> = _sections

    fun onNameChange(name: String) {
        state = state.copy(name = name)
    }

    fun onDescriptionChange(description: String) {
        state = state.copy(description = description)
    }
    fun setCollectionId(collectionId: String) {
        state = state.copy(collectionId = collectionId)
    }


    fun saveSection() {
        viewModelScope.launch(Dispatchers.IO) {
            SectionDAO().insert(state)
        }
    }
}