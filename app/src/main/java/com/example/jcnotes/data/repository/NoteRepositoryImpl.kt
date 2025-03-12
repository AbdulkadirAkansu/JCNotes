package com.example.jcnotes.data.repository

import com.example.jcnotes.data.dao.NoteDao
import com.example.jcnotes.data.model.Note
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val noteDao: NoteDao
) : NoteRepository {
    override fun getAllNotes(): Flow<List<Note>> = noteDao.getAllNotes()
    override fun getNotesOrderedByTitle(): Flow<List<Note>> = noteDao.getNotesOrderedByTitle()
    override fun getNotesOrderedByDate(): Flow<List<Note>> = noteDao.getNotesOrderedByDate()
    override fun getArchivedNotes(): Flow<List<Note>> = noteDao.getArchivedNotes()
    override fun getDeletedNotes(): Flow<List<Note>> = noteDao.getDeletedNotes()
    override suspend fun getNoteById(id: Int): Note? = noteDao.getNoteById(id)
    override suspend fun insert(note: Note) = noteDao.insert(note)
    override suspend fun delete(note: Note) = noteDao.delete(note)
    override suspend fun update(note: Note) = noteDao.update(note)
} 