package com.example.quiz_mobile.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.quiz_mobile.model.Task
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class TaskViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()
    private val tasksCollection = db.collection("tasks")

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks.asStateFlow()

    init {
        listenForTaskUpdates()
    }

    private fun listenForTaskUpdates() {
        tasksCollection.addSnapshotListener { snapshot, error ->
            if (error != null) {
                Log.w("TaskViewModel", "Listen failed.", error)
                return@addSnapshotListener
            }

            if (snapshot != null) {
                val taskList = snapshot.documents.mapNotNull { doc ->
                    val task = doc.toObject(Task::class.java)
                    task?.copy(id = doc.id)
                }

                _tasks.value = taskList.sortedByDescending { it.timestamp }

                Log.d("TaskViewModel", "Tasks updated: ${taskList.size} items")
                taskList.forEach { task ->
                    Log.d("TaskViewModel", "Task: id=${task.id}, name=${task.name}, completed=${task.isCompleted}")
                }
            }
        }
    }

    fun addTask(taskName: String) {
        if (taskName.isBlank()) return

        val newTask = hashMapOf(
            "name" to taskName,
            "isCompleted" to false,
            "timestamp" to FieldValue.serverTimestamp()
        )

        viewModelScope.launch {
            tasksCollection.add(newTask)
                .addOnSuccessListener {
                    Log.d("TaskViewModel", "Task added successfully")
                }
                .addOnFailureListener { e ->
                    Log.w("TaskViewModel", "Error adding task", e)
                }
        }
    }

    fun updateTaskStatus(taskId: String, isCompleted: Boolean) {
        Log.d("TaskViewModel", "📝 Attempting to update: taskId='$taskId', isCompleted=$isCompleted")

        if (taskId.isBlank()) {
            Log.e("TaskViewModel", "❌ Task ID is BLANK!")
            return
        }


        val currentTasks = _tasks.value
        val updatedTasks = currentTasks.map { task ->
            if (task.id == taskId) {
                task.copy(isCompleted = isCompleted)
            } else {
                task
            }
        }
        _tasks.value = updatedTasks.sortedByDescending { it.timestamp }


        viewModelScope.launch {
            tasksCollection.document(taskId)
                .update("isCompleted", isCompleted)
                .addOnSuccessListener {
                    Log.d("TaskViewModel", "✅ Update success for taskId='$taskId'")
                }
                .addOnFailureListener { e ->
                    Log.e("TaskViewModel", "❌ Error updating task '$taskId'", e)
                    // Rollback jika gagal
                    val rollbackTasks = _tasks.value.map { task ->
                        if (task.id == taskId) {
                            task.copy(isCompleted = !isCompleted)
                        } else {
                            task
                        }
                    }
                    _tasks.value = rollbackTasks.sortedByDescending { it.timestamp }
                }
        }
    }
}