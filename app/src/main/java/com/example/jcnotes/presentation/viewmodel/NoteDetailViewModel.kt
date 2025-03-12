package com.example.jcnotes.presentation.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jcnotes.data.model.Note
import com.example.jcnotes.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class NoteDetailViewModel @Inject constructor(
    private val repository: NoteRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val noteId: Int = checkNotNull(savedStateHandle["noteId"])
    
    private val _note = MutableStateFlow<Note?>(null)
    val note: StateFlow<Note?> = _note.asStateFlow()
    
    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title.asStateFlow()
    
    private val _content = MutableStateFlow("")
    val content: StateFlow<String> = _content.asStateFlow()
    
    private val _color = MutableStateFlow(0)
    val color: StateFlow<Int> = _color.asStateFlow()
    
    private val _isEditing = MutableStateFlow(false)
    val isEditing: StateFlow<Boolean> = _isEditing.asStateFlow()
    
    private val _isSaving = MutableStateFlow(false)
    val isSaving: StateFlow<Boolean> = _isSaving.asStateFlow()

    init {
        loadNote()
    }

    private fun loadNote() {
        viewModelScope.launch {
            repository.getAllNotes()
                .map { notes -> notes.find { it.id == noteId } }
                .collect { foundNote ->
                    foundNote?.let { loadedNote ->
                        _note.value = loadedNote
                        _title.value = loadedNote.title
                        _content.value = loadedNote.content
                        _color.value = loadedNote.color
                    }
                }
        }
    }

    fun updateTitle(newTitle: String) {
        _title.value = newTitle
    }

    fun updateContent(newContent: String) {
        _content.value = newContent
    }

    fun updateColor(newColor: Int) {
        _color.value = newColor
    }

    fun toggleEditing() {
        _isEditing.value = !_isEditing.value
        if (!_isEditing.value) {
            // Düzenleme modundan çıkıldığında orijinal değerlere dön
            _note.value?.let { originalNote ->
                _title.value = originalNote.title
                _content.value = originalNote.content
                _color.value = originalNote.color
            }
        }
    }

    fun updateNote(title: String, content: String, color: Int) {
        val currentNote = _note.value ?: return
        if (title.isBlank() && content.isBlank()) return

        viewModelScope.launch {
            _isSaving.value = true
            try {
                val updatedNote = currentNote.copy(
                    title = title,
                    content = content,
                    color = color,
                    timestamp = System.currentTimeMillis()
                )
                repository.update(updatedNote)
                _note.value = updatedNote
                _isEditing.value = false
            } finally {
                _isSaving.value = false
            }
        }
    }

    fun saveNote() {
        updateNote(_title.value, _content.value, _color.value)
    }


    fun deleteNote(onNoteDeleted: () -> Unit) {
        val currentNote = _note.value ?: return
        viewModelScope.launch {
            repository.delete(currentNote)
            onNoteDeleted()
        }
    }

    fun addNote(title: String, content: String, color: Int) {
        if (title.isBlank() && content.isBlank()) return
        
        viewModelScope.launch {
            _isSaving.value = true
            try {
                val note = Note(
                    title = title,
                    content = content,
                    color = color,
                    timestamp = System.currentTimeMillis(),
                )
                repository.insert(note)
            } finally {
                _isSaving.value = false
            }
        }
    }
}