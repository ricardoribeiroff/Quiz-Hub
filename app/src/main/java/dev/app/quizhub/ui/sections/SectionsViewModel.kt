package dev.app.quizhub.ui.sections

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import dev.app.quizhub.data.CollectionDAO
import dev.app.quizhub.data.SectionDAO
import dev.app.quizhub.model.CollectionEntity
import dev.app.quizhub.model.Section
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SectionsViewModel(application: Application) : AndroidViewModel(application) {

    private val _sections = MutableStateFlow<List<Section>>(emptyList())
    val sections: MutableStateFlow<List<Section>> = _sections

    fun fetchSections(collectionId: String) {
        viewModelScope.launch {
            _sections.value = SectionDAO().getByCollectionId(collectionId)
        }
    }
}