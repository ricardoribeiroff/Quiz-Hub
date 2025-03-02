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
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import android.content.Context
import kotlinx.serialization.encodeToString

@Serializable
data class QuestionState(
    val selectedAlternatives: Map<Long, Long>,
    val evaluationResults: Map<Long, Boolean>,
    val isEvaluated: Boolean
)

class QuestionsViewModel(application: Application) : AndroidViewModel(application) {
    private val context = application.applicationContext
    private val sharedPreferences = context.getSharedPreferences("quiz_state", Context.MODE_PRIVATE)

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

    private var currentSetId: String? = null

    private val _isReadOnly = MutableStateFlow(false)
    val isReadOnly: StateFlow<Boolean> = _isReadOnly

    fun fetchQuestions(setId: String) {
        currentSetId = setId
        viewModelScope.launch {
            try {
                _questions.value = QuestionDAO().getBySetId(setId)
                val allQuestionSets = QuestionSetDAO().getAll()
                _questionSet.value = allQuestionSets.find { it.id.toString() == setId }
                
                // Carrega o estado salvo primeiro
                loadSavedState(setId)
                
                // Depois verifica se o questionário está finalizado no banco
                _questionSet.value?.let { set ->
                    if (set.isFinished) {
                        _isReadOnly.value = true
                        _isEvaluated.value = true
                    }
                }
            } catch (e: Exception) {
                Log.e("QuestionsViewModel", "Erro ao carregar questões: ${e.message}")
            }
        }
    }

    private fun loadSavedState(setId: String) {
        val savedStateJson = sharedPreferences.getString("quiz_state_$setId", null)
        if (savedStateJson != null) {
            try {
                val savedState = Json.decodeFromString<QuestionState>(savedStateJson)
                _selectedAlternatives.value = savedState.selectedAlternatives
                _evaluationResult.value = savedState.evaluationResults
                _isEvaluated.value = savedState.isEvaluated
                if (savedState.isEvaluated) {
                    _isReadOnly.value = true
                }
            } catch (e: Exception) {
                Log.e("QuestionsViewModel", "Error loading saved state", e)
            }
        }
    }

    private fun saveState() {
        currentSetId?.let { setId ->
            val state = QuestionState(
                selectedAlternatives = _selectedAlternatives.value,
                evaluationResults = _evaluationResult.value,
                isEvaluated = _isEvaluated.value
            )
            try {
                val stateJson = Json.encodeToString(state)
                sharedPreferences.edit().putString("quiz_state_$setId", stateJson).apply()
            } catch (e: Exception) {
                Log.e("QuestionsViewModel", "Error saving state", e)
            }
        }
    }

    fun fetchAlternatives() {
        viewModelScope.launch {
            try {
                _alternatives.value = AlternativeDAO().getAll()
            } catch (e: Exception) {
                Log.e("QuestionsViewModel", "Erro ao carregar alternativas: ${e.message}")
            }
        }
    }

    fun toggleAlternativeSelection(questionId: Long, alternativeId: Long) {
        // Se estiver em modo somente leitura, não permite alterações
        if (_isReadOnly.value) return

        val currentSelections = _selectedAlternatives.value.toMutableMap()
        if (currentSelections[questionId] == alternativeId) {
            currentSelections.remove(questionId)
        } else {
            currentSelections[questionId] = alternativeId
        }
        _selectedAlternatives.value = currentSelections
        _isEvaluated.value = false
        saveState()
        Log.d("DEBUGLOG", "Question id $questionId selected alternative id: $alternativeId")
    }

    suspend fun evaluateAnswers(): Boolean {
        if (_selectedAlternatives.value.size < _questions.value.size) {
            Log.d("EVALUATION", "Please answer all questions before evaluation.")
            return false
        }

        val results = mutableMapOf<Long, Boolean>()
        val evaluatedAlternatives = mutableListOf<Long>()

        _selectedAlternatives.value.forEach { (questionId, alternativeId) ->
            val chosenAlternative = _alternatives.value.firstOrNull { it.id == alternativeId }
            if (chosenAlternative != null) {
                results[questionId] = chosenAlternative.isCorrect
                evaluatedAlternatives.add(alternativeId)
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
        _isReadOnly.value = true
        saveState()

        try {
            // Atualiza o status do QuestionSet
            currentSetId?.let { setId ->
                QuestionSetDAO().updateIsFinished(setId, true)
            }

            // Atualiza apenas as alternativas que foram efetivamente avaliadas
            evaluatedAlternatives.forEach { alternativeId ->
                AlternativeDAO().updateIsFinished(alternativeId, true)
            }
            
            // Recarrega as alternativas para obter os estados atualizados
            fetchAlternatives()
            
            return true
        } catch (e: Exception) {
            Log.e("EVALUATION", "Erro ao atualizar status: ${e.message}")
            return false
        }
    }

    fun evaluateAndNavigate(onComplete: (Boolean) -> Unit) {
        viewModelScope.launch {
            val result = evaluateAnswers()
            onComplete(result)
        }
    }
}