package com.example.quiz_mobile // Lokasi file-mu

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.quiz_mobile.ui.TaskListScreen
import com.example.quiz_mobile.ui.theme.Quiz_mobileTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Membungkus screen-mu dengan tema
            Quiz_mobileTheme {
                TaskListScreen()
            }
        }
    }
}