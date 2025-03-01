package dev.app.quizhub.ui.shared

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val _collectionId = MutableLiveData<String>()
    val collectionId: LiveData<String> get() = _collectionId

    private val _sectionId = MutableLiveData<String>()
    val sectionId: LiveData<String> get() = _sectionId

    private val _setId = MutableLiveData<String>()
    val setId: LiveData<String> get() = _setId


    fun setCollectionId(id: String) {
        _collectionId.value = id
    }
    fun setSectionId(id: String) {
        _sectionId.value = id
    }
    fun setSetId(id: String) {
        _setId.value = id
    }
}