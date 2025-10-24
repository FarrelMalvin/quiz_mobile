package com.example.quiz_mobile.util

import com.google.firebase.Timestamp
import java.text.SimpleDateFormat
import java.util.Locale


fun formatTimestamp(timestamp: Timestamp?): String {
    if (timestamp == null) return "Waktu tidak tersedia"


    val sdf = SimpleDateFormat("dd:MM:yyyy – HH:mm", Locale.getDefault())
    return sdf.format(timestamp.toDate())
}