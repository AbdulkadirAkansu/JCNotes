package com.example.jcnotes.data.repository

import com.example.jcnotes.data.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    fun getAllNotes(): Flow<List<Note>>
    fun getNotesOrderedByTitle(): Flow<List<Note>>
    fun getNotesOrderedByDate(): Flow<List<Note>>
    fun getArchivedNotes(): Flow<List<Note>>
    fun getDeletedNotes(): Flow<List<Note>>
    suspend fun getNoteById(id: Int): Note?
    suspend fun insert(note: Note)
    suspend fun delete(note: Note)
    suspend fun update(note: Note)
}