package dev.app.quizhub.ui.home

import android.app.Application
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.app.quizhub.data.CollectionDAO
import dev.app.quizhub.model.CollectionEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val dao = CollectionDAO(application)
    var collection by mutableStateOf<List<CollectionEntity>>(listOf())
        private set

    init {
        viewModelScope.launch {
            collection = dao.getAll()
        }
    }
}