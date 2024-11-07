package com.example.jcnotes.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.jcnotes.data.model.Note
import com.example.jcnotes.data.repository.NoteRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel(){

    val notes: StateFlow<List<Note>> = repository.getAllNotes()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    fun insertNote(note: Note){
        viewModelScope.launch {
            repository.insert(note)
        }
    }

    fun deleteNote(note: Note){
        viewModelScope.launch {
            repository.Delete(note)
        }
    }

    fun getNoteById(noteId: Int): Flow<Note?> {
        return repository.getAllNotes()
            .map { notes -> notes.find { it.id == noteId } }
    }

    fun updateNote(note: Note){
        viewModelScope.launch {
            repository.Update(note)
        }
    }

}