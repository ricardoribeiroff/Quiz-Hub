package dev.app.quizhub.ui.sections

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dev.app.quizhub.data.QuestionSetDAO
import dev.app.quizhub.model.QuestionSet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class QuestionSetsViewModel(application: Application) : AndroidViewModel(application) {

    private val _questionSets = MutableStateFlow<List<QuestionSet>>(emptyList())
    val questionSet: MutableStateFlow<List<QuestionSet>> = _questionSets

    fun fetchSections(sectionId: String) {
        viewModelScope.launch {
            _questionSets.value = QuestionSetDAO().getBySectionId(sectionId)
        }
    }
}