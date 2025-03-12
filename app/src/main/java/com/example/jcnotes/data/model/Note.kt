package com.example.jcnotes.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.jcnotes.data.converter.Converters

@Entity(tableName = "notes")
@TypeConverters(Converters::class)
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String = "",
    val content: String = "",
    val timestamp: Long? = null,
    val isArchived: Int = 0,
    val isDeleted: Int = 0,
    val tasks: List<Task>? = null,
    val progress: Float = 0f,
    val color: Int = 0,
    val category: String = "All",
    
    // Yeni alanlar
    val tags: List<String>? = null, // Etiketler
    val reminderTime: Long? = null, // Hatırlatıcı zamanı
    val images: List<String>? = null, // Resim yolları
    val audioRecordings: List<String>? = null, // Ses kaydı yolları
    val favorite: Boolean = false, // isFavorite yerine favorite olarak değiştirildi ve Boolean tipine çevrildi
    val lastEditedTime: Long = System.currentTimeMillis(), // Son düzenleme zamanı
    val textFormatting: Map<String, Any>? = null, // Zengin metin biçimlendirme bilgileri
    val folderId: Int? = null, // Klasör ID'si
    val commentCount: Int = 0 // Yorum sayısı
)

