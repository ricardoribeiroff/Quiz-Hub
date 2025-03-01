package dev.app.quizhub.ui.shared

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _collectionId = MutableLiveData<String>()
    val collectionId: LiveData<String> get() = _collectionId

    fun setCollectionId(id: String) {
        _collectionId.value = id
    }
}