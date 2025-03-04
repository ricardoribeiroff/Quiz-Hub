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
import io.github.jan.supabase.exceptions.HttpRequestException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withTimeout
import kotlinx.coroutines.TimeoutCancellationException
import kotlinx.coroutines.withContext

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

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _showTimeoutDialog = MutableStateFlow(false)
    val showTimeoutDialog: StateFlow<Boolean> = _showTimeoutDialog

    private var currentSetId: String? = null

    private val _isReadOnly = MutableStateFlow(false)
    val isReadOnly: StateFlow<Boolean> = _isReadOnly

    private suspend fun <T> executeWithTimeout(
        block: suspend () -> T
    ): T {
        if (_isLoading.value) {
            throw IllegalStateException("Operação em andamento")
        }

        _isLoading.value = true
        _showTimeoutDialog.value = false
        _errorMessage.value = null

        return try {
            withContext(Dispatchers.IO) {
                withTimeout(10000) {
                    block()
                }
            }
        } catch (e: TimeoutCancellationException) {
            _showTimeoutDialog.value = true
            Log.e("QuestionsViewModel", "Timeout na operação: ${e.message}", e)
            throw e
        } catch (e: HttpRequestException) {
            Log.e("QuestionsViewModel", "Erro de rede: ${e.message}", e)
            _errorMessage.value = "Sem conexão com a internet. Verifique sua conexão e tente novamente."
            throw e
        } catch (e: Exception) {
            Log.e("QuestionsViewModel", "Erro na operação: ${e.message}", e)
            _errorMessage.value = "Erro ao processar operação. Tente novamente."
            throw e
        } finally {
            if (!_showTimeoutDialog.value) {
                _isLoading.value = false
            }
        }
    }

    fun fetchQuestions(setId: String) {
        currentSetId = setId
        viewModelScope.launch(Dispatchers.Main) {
            try {
                executeWithTimeout {
                    val questions = QuestionDAO().getBySetId(setId)
                    val allQuestionSets = QuestionSetDAO().getAll()
                    val questionSet = allQuestionSets.find { it.id.toString() == setId }

                    withContext(Dispatchers.Main) {
                        // Atualiza os estados de forma segura na Main thread
                        _questions.value = questions
                        _questionSet.value = questionSet

                        // Carrega o estado salvo primeiro
                        loadSavedState(setId)

                        // Depois verifica se o questionário está finalizado no banco
                        questionSet?.let { set ->
                            if (set.isFinished) {
                                _isReadOnly.value = true
                                _isEvaluated.value = true
                            } else {
                                // Se não estiver finalizado, reseta os estados visuais
                                _isReadOnly.value = false
                                _isEvaluated.value = false
                            }
                        }
                    }
                }

                // Carrega as alternativas após carregar as questões
                fetchAlternatives()
            } catch (e: Exception) {
                Log.e("QuestionsViewModel", "Erro ao carregar questões: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    _errorMessage.value = when {
                        e.message?.contains("Unable to resolve host") == true ->
                            "Sem conexão com a internet. Verifique sua conexão."
                        else -> "Erro ao carregar dados. Tente novamente."
                    }
                }
                throw e
            } finally {
                withContext(Dispatchers.Main) {
                    if (!_showTimeoutDialog.value) {
                        _isLoading.value = false
                    }
                }
            }
        }
    }

    private fun loadSavedState(setId: String) {
        try {
            val savedStateJson = sharedPreferences.getString("quiz_state_$setId", null)
            if (savedStateJson != null) {
                val savedState = Json.decodeFromString<QuestionState>(savedStateJson)
                _selectedAlternatives.value = savedState.selectedAlternatives

                // Só carrega os resultados da avaliação se o questionário estiver finalizado
                if (_questionSet.value?.isFinished == true) {
                    _evaluationResult.value = savedState.evaluationResults
                    _isEvaluated.value = savedState.isEvaluated
                    if (savedState.isEvaluated) {
                        _isReadOnly.value = true
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("QuestionsViewModel", "Error loading saved state", e)
            _errorMessage.value = "Erro ao carregar estado salvo."
        }
    }

    private fun saveState() {
        currentSetId?.let { setId ->
            try {
                val state = QuestionState(
                    selectedAlternatives = _selectedAlternatives.value,
                    evaluationResults = _evaluationResult.value,
                    isEvaluated = _isEvaluated.value
                )
                val stateJson = Json.encodeToString(state)
                sharedPreferences.edit().putString("quiz_state_$setId", stateJson).apply()
            } catch (e: Exception) {
                Log.e("QuestionsViewModel", "Error saving state", e)
                _errorMessage.value = "Erro ao salvar estado."
            }
        }
    }

    fun fetchAlternatives() {
        viewModelScope.launch {
            try {
                _alternatives.value = AlternativeDAO().getAll().sortedBy { it.id }
            } catch (e: Exception) {
                Log.e("QuestionsViewModel", "Erro ao carregar alternativas: ${e.message}", e)
                _errorMessage.value = "Erro ao carregar alternativas. Tente novamente."
            }
        }
    }

    fun toggleAlternativeSelection(questionId: Long, alternativeId: Long) {
        if (_isReadOnly.value) return

        try {
            val currentSelections = _selectedAlternatives.value.toMutableMap()
            if (currentSelections[questionId] == alternativeId) {
                currentSelections.remove(questionId)
            } else {
                currentSelections[questionId] = alternativeId
            }
            _selectedAlternatives.value = currentSelections
            _isEvaluated.value = false
            saveState()
        } catch (e: Exception) {
            Log.e("QuestionsViewModel", "Erro ao selecionar alternativa: ${e.message}")
            _errorMessage.value = "Erro ao selecionar alternativa."
        }
    }

    fun evaluateAndNavigate(onComplete: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.Main) {
            try {
                withContext(Dispatchers.IO) {
                    val result = evaluateAnswers()
                    withContext(Dispatchers.Main) {
                        if (result) {
                            onComplete(true)
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("QuestionsViewModel", "Erro ao avaliar: ${e.message}", e)
                withContext(Dispatchers.Main) {
                    _errorMessage.value = when {
                        e.message?.contains("Unable to resolve host") == true -> 
                            "Sem conexão com a internet. Suas respostas foram salvas localmente."
                        else -> "Erro ao processar avaliação. Suas respostas foram salvas localmente."
                    }
                }
            } finally {
                withContext(Dispatchers.Main) {
                    if (!_showTimeoutDialog.value) {
                        _isLoading.value = false
                    }
                }
            }
        }
    }

    suspend fun evaluateAnswers(): Boolean {
        if (_selectedAlternatives.value.size < _questions.value.size) {
            withContext(Dispatchers.Main) {
                _errorMessage.value = "Por favor, responda todas as questões antes de avaliar."
            }
            return false
        }

        return try {
            executeWithTimeout {
                val results = mutableMapOf<Long, Boolean>()
                val evaluatedAlternatives = mutableListOf<Long>()

                // Primeiro atualiza o banco de dados
                currentSetId?.let { setId ->
                    try {
                        QuestionSetDAO().updateIsFinished(setId, true)
                        withContext(Dispatchers.Main) {
                            // Atualiza o QuestionSet local de forma segura
                            _questionSet.value = _questionSet.value?.copy(isFinished = true)
                        }
                    } catch (e: Exception) {
                        Log.e("QuestionsViewModel", "Erro ao atualizar status no banco: ${e.message}", e)
                        withContext(Dispatchers.Main) {
                            _errorMessage.value = "Erro ao salvar avaliação. Suas respostas foram salvas localmente."
                        }
                        throw e
                    }
                }

                // Avalia apenas as alternativas que foram selecionadas pelo usuário
                _selectedAlternatives.value.forEach { (questionId, alternativeId) ->
                    val chosenAlternative = _alternatives.value.firstOrNull { it.id == alternativeId }
                    if (chosenAlternative != null) {
                        results[questionId] = chosenAlternative.isCorrect
                        evaluatedAlternatives.add(alternativeId)
                        try {
                            // Marca apenas a alternativa selecionada como finalizada
                            AlternativeDAO().updateIsFinished(alternativeId, true)
                        } catch (e: Exception) {
                            Log.e("QuestionsViewModel", "Erro ao atualizar alternativa: ${e.message}", e)
                            // Continua a execução mesmo se falhar ao atualizar uma alternativa
                        }
                    } else {
                        results[questionId] = false
                    }
                }

                withContext(Dispatchers.Main) {
                    // Atualiza os estados locais de forma segura
                    _evaluationResult.value = results
                    _isEvaluated.value = true
                    _isReadOnly.value = true
                    saveState()
                }

                true
            }
        } catch (e: TimeoutCancellationException) {
            withContext(Dispatchers.Main) {
                _showTimeoutDialog.value = true
            }
            false
        } catch (e: Exception) {
            Log.e("EVALUATION", "Erro ao atualizar status: ${e.message}", e)
            withContext(Dispatchers.Main) {
                _errorMessage.value = when {
                    e.message?.contains("Unable to resolve host") == true -> 
                        "Sem conexão com a internet. Suas respostas foram salvas localmente."
                    else -> "Erro ao processar avaliação. Suas respostas foram salvas localmente."
                }
            }
            false
        }
    }

    fun dismissTimeoutDialog() {
        _showTimeoutDialog.value = false
        _isLoading.value = false // Garante que o loading seja fechado
    }
}