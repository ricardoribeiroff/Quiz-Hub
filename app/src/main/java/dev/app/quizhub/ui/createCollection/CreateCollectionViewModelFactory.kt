package dev.app.quizhub.ui.createCollection

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import dev.app.quizhub.navigation.Screen

class CreateCollectionViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateCollectionViewModel::class.java)) {
            return CreateCollectionViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}