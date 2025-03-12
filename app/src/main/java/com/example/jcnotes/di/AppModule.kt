package com.example.jcnotes.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.jcnotes.data.database.NoteDatabase
import com.example.jcnotes.data.repository.NoteRepository
import com.example.jcnotes.data.dao.NoteDao
import com.example.jcnotes.data.repository.NoteRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    
    // Migration from 1 to 2 - color field added
    private val MIGRATION_1_2 = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            // SQLite'da ALTER TABLE ile doğrudan sütun eklemek mümkün
            database.execSQL("ALTER TABLE notes ADD COLUMN color INTEGER NOT NULL DEFAULT ${0xFFFFFFFF.toInt()}")
        }
    }
    
    @Provides
    @Singleton
    fun provideNoteDatabase(
        @ApplicationContext app: Context
    ): NoteDatabase {
        return NoteDatabase.getDatabase(app)
    }

    @Provides
    @Singleton
    fun provideNoteDao(database: NoteDatabase): NoteDao {
        return database.noteDao()
    }

    @Provides
    @Singleton
    fun provideNoteRepository(noteDao: NoteDao): NoteRepository {
        return NoteRepositoryImpl(noteDao)
    }
}
