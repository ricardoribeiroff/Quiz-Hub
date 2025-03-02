package dev.app.quizhub.ui.createQuestionSet

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dev.app.quizhub.data.QuestionSetDAO
import dev.app.quizhub.model.QuestionSet
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CreateQuestionSetViewModel(application: Application) : AndroidViewModel(application) {

    var state by mutableStateOf(QuestionSet())
        private set

    fun onNameChange(name: String) {
        state = state.copy(name = name)
    }

    fun onDescriptionChange(description: String) {
        state = state.copy(description = description)
    }
    
    fun setSectionId(sectionId: String) {
        state = state.copy(sectionId = sectionId.toInt())
    }

    fun saveQuestionSet() {
        viewModelScope.launch(Dispatchers.IO) {
            QuestionSetDAO().insert(state)
        }
    }
}