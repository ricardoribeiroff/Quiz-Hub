package dev.app.quizhub.ui.sections

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class SectionsViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(QuestionSetsViewModel::class.java)) {
            return QuestionSetsViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}