package com.example.jcnotes.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.jcnotes.data.converter.Converters
import com.example.jcnotes.data.dao.FolderDao
import com.example.jcnotes.data.dao.NoteDao
import com.example.jcnotes.data.model.Folder
import com.example.jcnotes.data.model.Note

// Veritabanı versiyonunu 2'ye yükseltip favorite alanı için migration ekleyeceğim.
@Database(entities = [Note::class, Folder::class], version = 2, exportSchema = true)
@TypeConverters(Converters::class)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao
    abstract fun folderDao(): FolderDao

    companion object {
        @Volatile
        private var INSTANCE: NoteDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Geçici tablo oluştur
                database.execSQL("""
                    CREATE TABLE IF NOT EXISTS notes_temp (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        title TEXT NOT NULL DEFAULT '',
                        content TEXT NOT NULL DEFAULT '',
                        timestamp INTEGER,
                        isArchived INTEGER NOT NULL DEFAULT 0,
                        isDeleted INTEGER NOT NULL DEFAULT 0,
                        tasks TEXT,
                        progress REAL NOT NULL DEFAULT 0,
                        color INTEGER NOT NULL DEFAULT 0,
                        category TEXT NOT NULL DEFAULT 'All',
                        tags TEXT,
                        reminderTime INTEGER,
                        images TEXT,
                        audioRecordings TEXT,
                        favorite INTEGER NOT NULL DEFAULT 0,
                        lastEditedTime INTEGER NOT NULL DEFAULT 0,
                        textFormatting TEXT,
                        folderId INTEGER,
                        commentCount INTEGER NOT NULL DEFAULT 0
                    )
                """)

                // Verileri geçici tabloya kopyala
                database.execSQL("""
                    INSERT OR IGNORE INTO notes_temp 
                    SELECT id, title, content, timestamp, isArchived, isDeleted, tasks,
                           progress, color, category, tags, reminderTime, images,
                           audioRecordings, 0, lastEditedTime, textFormatting,
                           folderId, commentCount
                    FROM notes
                """)

                // Eski tabloyu sil
                database.execSQL("DROP TABLE IF EXISTS notes")

                // Geçici tabloyu yeniden adlandır
                database.execSQL("ALTER TABLE notes_temp RENAME TO notes")
            }
        }

        fun getDatabase(context: Context): NoteDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    NoteDatabase::class.java,
                    "note_database"
                )
                .addMigrations(MIGRATION_1_2)
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}