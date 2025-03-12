package com.example.jcnotes.data.model

data class Task(
    val id: String = java.util.UUID.randomUUID().toString(),
    val text: String,
    val completed: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val dueDate: Long? = null,
    val priority: TaskPriority = TaskPriority.NORMAL,
    val subTasks: List<SubTask>? = null
)

data class SubTask(
    val id: String = java.util.UUID.randomUUID().toString(),
    val text: String,
    val completed: Boolean = false
)

enum class TaskPriority {
    LOW, NORMAL, HIGH, URGENT
} 