package com.example.jcnotes.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.jcnotes.data.model.Note
import kotlinx.coroutines.flow.Flow


@Dao
interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun Insert(note: Note)

    @Update
    suspend fun Update(note: Note)

    @Delete
    suspend fun Delete(note: Note)

    @Query("SELECT * FROM notes ORDER BY timestamp DESC")
    fun GetAllNotes(): Flow<List<Note>>
}