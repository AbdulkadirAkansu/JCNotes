package com.example.jcnotes.data.converter

import androidx.room.TypeConverter
import com.example.jcnotes.data.model.SubTask
import com.example.jcnotes.data.model.Task
import com.example.jcnotes.data.model.TaskPriority
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.Date

class Converters {
    private val gson = Gson()

    // Task Converters
    @TypeConverter
    fun fromTaskList(value: List<Task>?): String? {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toTaskList(value: String?): List<Task>? {
        if (value == null) return null
        val listType = object : TypeToken<List<Task>>() {}.type
        return gson.fromJson(value, listType)
    }

    // String List Converters
    @TypeConverter
    fun fromStringList(value: List<String>?): String? {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toStringList(value: String?): List<String>? {
        if (value == null) return null
        val listType = object : TypeToken<List<String>>() {}.type
        return gson.fromJson(value, listType)
    }
    
    // SubTask Converters
    @TypeConverter
    fun fromSubTaskList(value: List<SubTask>?): String? {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toSubTaskList(value: String?): List<SubTask>? {
        if (value == null) return null
        val listType = object : TypeToken<List<SubTask>>() {}.type
        return gson.fromJson(value, listType)
    }
    
    // TaskPriority Converters
    @TypeConverter
    fun fromTaskPriority(value: TaskPriority): String {
        return value.name
    }

    @TypeConverter
    fun toTaskPriority(value: String): TaskPriority {
        return try {
            TaskPriority.valueOf(value)
        } catch (e: Exception) {
            TaskPriority.NORMAL
        }
    }
    
    // Map Converters for text formatting
    @TypeConverter
    fun fromMap(value: Map<String, Any>?): String? {
        return gson.toJson(value)
    }

    @TypeConverter
    fun toMap(value: String?): Map<String, Any>? {
        if (value == null) return null
        val mapType = object : TypeToken<Map<String, Any>>() {}.type
        return gson.fromJson(value, mapType)
    }
} 