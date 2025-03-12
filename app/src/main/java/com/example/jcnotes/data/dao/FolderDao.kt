package com.example.jcnotes.data.dao

import androidx.room.*
import com.example.jcnotes.data.model.Folder
import kotlinx.coroutines.flow.Flow

@Dao
interface FolderDao {
    @Query("SELECT * FROM folders ORDER BY name ASC")
    fun getAllFolders(): Flow<List<Folder>>
    
    @Query("SELECT * FROM folders WHERE id = :folderId")
    fun getFolderById(folderId: Int): Flow<Folder?>
    
    @Query("SELECT * FROM folders WHERE parentFolderId = :parentFolderId")
    fun getSubFolders(parentFolderId: Int): Flow<List<Folder>>
    
    @Query("SELECT * FROM folders WHERE isDefault = 1 LIMIT 1")
    fun getDefaultFolder(): Flow<Folder?>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFolder(folder: Folder): Long
    
    @Update
    suspend fun updateFolder(folder: Folder)
    
    @Delete
    suspend fun deleteFolder(folder: Folder)
    
    @Query("DELETE FROM folders WHERE id = :folderId")
    suspend fun deleteFolderById(folderId: Int)
    
    @Query("SELECT COUNT(*) FROM notes WHERE folderId = :folderId")
    suspend fun getNotesCountInFolder(folderId: Int): Int
} 