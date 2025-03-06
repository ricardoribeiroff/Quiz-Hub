package dev.app.quizhub.ui.createQuestionsAI

import android.app.Application
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dev.app.quizhub.data.ApiService
import dev.app.quizhub.model.AIQuery
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class CreateQuestionsAIViewModel(application: Application) : AndroidViewModel(application) {

    var state by mutableStateOf(AIQuery())
        private set
    val apiService = ApiService()

    fun onQuestionsThemeChange(questionsTheme: String) {
        state = state.copy(questionsTheme = questionsTheme)
    }

    fun onQuestionsAmountChange(questionsAmount: String) {
        state = state.copy(questionsAmount = questionsAmount)
    }

    fun onQuestionsDifficultyChange(questionsDifficulty: String) {
        state = state.copy(questionsDifficulty = questionsDifficulty)
    }

    fun getQuestions() {
        viewModelScope.launch(Dispatchers.IO) {
            apiService.getQuestions(state)
        }
    }

}