package com.example.jcnotes.data.repository

import androidx.room.Update
import com.example.jcnotes.data.dao.NoteDao
import com.example.jcnotes.data.model.Note
import kotlinx.coroutines.flow.Flow

class NoteRepository(private val noteDao: NoteDao) {
    fun getAllNotes(): Flow<List<Note>> = noteDao.GetAllNotes()

    suspend fun insert(note: Note){
        noteDao.Insert(note)
    }
    suspend fun Update(note: Note){
        noteDao.Update(note)
    }
    suspend fun Delete(note: Note){
        noteDao.Delete(note)
    }
}