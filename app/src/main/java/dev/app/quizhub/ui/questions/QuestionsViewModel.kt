package dev.app.quizhub.ui.questions

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dev.app.quizhub.data.AlternativeDAO
import dev.app.quizhub.data.QuestionDAO
import dev.app.quizhub.data.QuestionSetDAO
import dev.app.quizhub.model.Alternative
import dev.app.quizhub.model.Question
import dev.app.quizhub.model.QuestionSet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class QuestionsViewModel(application: Application) : AndroidViewModel(application) {

    private val _questions = MutableStateFlow<List<Question>>(emptyList())
    val question: MutableStateFlow<List<Question>> = _questions

    private val _alternatives = MutableStateFlow<List<Alternative>>(emptyList())
    val alternative: MutableStateFlow<List<Alternative>> = _alternatives

    fun fetchQuestions(setId: String) {
        viewModelScope.launch {
            _questions.value = QuestionDAO().getBySetId(setId)
        }
    }
    fun fetchAlternatives(questionId: String) {
        viewModelScope.launch {
            _alternatives.value = AlternativeDAO().getByQuestionId(questionId)
        }
    }

}