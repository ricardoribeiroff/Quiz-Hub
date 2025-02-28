package dev.app.quizhub.ui.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dev.app.quizhub.data.CollectionDAO
import dev.app.quizhub.model.CollectionEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val _collections = MutableStateFlow<List<CollectionEntity>>(emptyList())
    val collections: StateFlow<List<CollectionEntity>> = _collections

    fun fetchCollections() {
        viewModelScope.launch {
            _collections.value = CollectionDAO().getAll()
        }
    }
}