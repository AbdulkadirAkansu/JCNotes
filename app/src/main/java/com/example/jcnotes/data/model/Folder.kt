package com.example.jcnotes.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "folders")
data class Folder(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val color: Int = 0xFF1A73E8.toInt(), // Varsayılan mavi renk
    val icon: String? = null, // İkon adı veya yolu
    val createdAt: Long = System.currentTimeMillis(),
    val lastModified: Long = System.currentTimeMillis(),
    val isDefault: Boolean = false, // Varsayılan klasör mü?
    val parentFolderId: Int? = null // İç içe klasörler için
) 