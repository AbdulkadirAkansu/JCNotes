package com.example.jcnotes.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jcnotes.data.model.Note
import com.example.jcnotes.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddNoteViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title.asStateFlow()

    private val _content = MutableStateFlow("")
    val content: StateFlow<String> = _content.asStateFlow()

    private val _color = MutableStateFlow(0)
    val color: StateFlow<Int> = _color.asStateFlow()

    private val _category = MutableStateFlow("All")
    val category: StateFlow<String> = _category.asStateFlow()

    private val _showColorPicker = MutableStateFlow(false)
    val showColorPicker: StateFlow<Boolean> = _showColorPicker.asStateFlow()

    private val _showCategoryPicker = MutableStateFlow(false)
    val showCategoryPicker: StateFlow<Boolean> = _showCategoryPicker.asStateFlow()

    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    fun updateTitle(newTitle: String) {
        _title.value = newTitle
    }

    fun updateContent(newContent: String) {
        _content.value = newContent
    }

    fun updateColor(newColor: Int) {
        _color.value = newColor
        _showColorPicker.value = false
    }

    fun updateCategory(newCategory: String) {
        _category.value = newCategory
        _showCategoryPicker.value = false
    }

    fun toggleColorPicker() {
        _showColorPicker.value = !_showColorPicker.value
    }

    fun toggleCategoryPicker() {
        _showCategoryPicker.value = !_showCategoryPicker.value
    }

    fun saveNote() {
        if (_title.value.isBlank() && _content.value.isBlank()) return
        
        viewModelScope.launch {
            _isSaving.value = true
            try {
                val note = Note(
                    title = _title.value,
                    content = _content.value,
                    color = _color.value,
                    category = _category.value,
                    timestamp = System.currentTimeMillis()
                )
                repository.insert(note)
                clearFields()
            } finally {
                _isSaving.value = false
            }
        }
    }

    private fun clearFields() {
        _title.value = ""
        _content.value = ""
        _color.value = 0
        _category.value = "All"
    }
} 