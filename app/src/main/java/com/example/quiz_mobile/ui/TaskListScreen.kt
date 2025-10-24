package com.example.quiz_mobile.ui
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.quiz_mobile.viewmodel.TaskViewModel
import com.example.quiz_mobile.util.formatTimestamp// <-- DIUBAH

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskListScreen(
    viewModel: TaskViewModel = viewModel() // Mengambil instance ViewModel
) {
    // Mengamati list kegiatan dari ViewModel
    val tasks by viewModel.tasks.collectAsState()

    // State untuk input teks
    var taskNameInput by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Daftar Kegiatan") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // 1. Input Area
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = taskNameInput,
                    onValueChange = { taskNameInput = it },
                    label = { Text("Kegiatan baru...") },
                    modifier = Modifier.weight(1f),
                    singleLine = true
                )
                Button(
                    onClick = {
                        viewModel.addTask(taskNameInput)
                        taskNameInput = "" // Kosongkan input setelah ditambah
                    },
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Text("Tambah")
                }
            }

            // 2. List Kegiatan (Menggunakan LazyColumn)
            LazyColumn(
                modifier = Modifier.weight(1f) // Memenuhi sisa ruang
            ) {
                items(tasks, key = { it.id }) { task ->
                    TaskItemCard(
                        task = task,
                        onCheckedChange = { isChecked ->
                            // Panggil fungsi update di ViewModel
                            viewModel.updateTaskStatus(task.id, isChecked)
                        }
                    )
                }
            }
        }
    }
}