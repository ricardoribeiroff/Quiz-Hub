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
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class QuestionsViewModel(application: Application) : AndroidViewModel(application) {

    private val _questions = MutableStateFlow<List<Question>>(emptyList())
    val question: StateFlow<List<Question>> = _questions

    private val _alternatives = MutableStateFlow<List<Alternative>>(emptyList())
    val alternative: StateFlow<List<Alternative>> = _alternatives

    private val _selectedAlternatives = MutableStateFlow<Map<Long, Long>>(emptyMap())
    val selectedAlternatives: StateFlow<Map<Long, Long>> = _selectedAlternatives

    private val _evaluationResult = MutableStateFlow<Map<Long, Boolean>>(emptyMap())
    val evaluationResult: StateFlow<Map<Long, Boolean>> = _evaluationResult

    private val _isEvaluated = MutableStateFlow(false)
    val isEvaluated: StateFlow<Boolean> = _isEvaluated

    private val _questionSet = MutableStateFlow<QuestionSet?>(null)
    val questionSet: StateFlow<QuestionSet?> = _questionSet

    fun fetchQuestions(setId: String) {
        viewModelScope.launch {
            _questions.value = QuestionDAO().getBySetId(setId)
            val allQuestionSets = QuestionSetDAO().getAll()
            _questionSet.value = allQuestionSets.find { it.id.toString() == setId }
        }
    }

    fun fetchAlternatives() {
        viewModelScope.launch {
            _alternatives.value = AlternativeDAO().getAll()
        }
    }

    fun toggleAlternativeSelection(questionId: Long, alternativeId: Long) {
        val currentSelections = _selectedAlternatives.value.toMutableMap()
        if (currentSelections[questionId] == alternativeId) {
            currentSelections.remove(questionId)
        } else {
            currentSelections[questionId] = alternativeId
        }
        _selectedAlternatives.value = currentSelections
        _isEvaluated.value = false // Reset evaluation when selection changes
        Log.d("DEBUGLOG", "Question id $questionId selected alternative id: $alternativeId")
    }

    fun evaluateAnswers(): Boolean {
        if (_selectedAlternatives.value.size < _questions.value.size) {
            Log.d("EVALUATION", "Please answer all questions before evaluation.")
            return false
        }

        val results = mutableMapOf<Long, Boolean>()
        _selectedAlternatives.value.forEach { (questionId, alternativeId) ->
            val chosenAlternative = _alternatives.value.firstOrNull { it.id == alternativeId }
            if (chosenAlternative != null) {
                results[questionId] = chosenAlternative.isCorrect
                Log.d(
                    "EVALUATION",
                    "Question id $questionId: Alternative id $alternativeId isCorrect = ${chosenAlternative.isCorrect}"
                )
            } else {
                Log.d("EVALUATION", "Question id $questionId: No alternative found")
                results[questionId] = false
            }
        }
        _evaluationResult.value = results
        _isEvaluated.value = true
        return true
    }
}