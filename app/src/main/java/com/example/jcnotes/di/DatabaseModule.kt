package com.example.jcnotes.di

import android.content.Context
import com.example.jcnotes.data.dao.NoteDao
import com.example.jcnotes.data.database.NoteDatabase
import com.example.jcnotes.data.repository.NoteRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {


    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): NoteDatabase{
        return NoteDatabase.getDatabase(appContext)
    }

    @Provides
    fun provideNoteDao(database: NoteDatabase): NoteDao{
        return database.noteDao()
    }


    @Provides
    fun provideNoteRepository(noteDao: NoteDao) : NoteRepository{
        return NoteRepository(noteDao)
    }
}