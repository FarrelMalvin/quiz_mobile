package com.example.quiz_mobile.model // <-- Pastikan package-mu benar

import com.google.firebase.Timestamp
import com.google.firebase.firestore.DocumentId

/**
 * Data class untuk merepresentasikan satu kegiatan.
 * Default value (misal: = "") WAJIB ADA agar Firestore 'toObjects()' berfungsi.
 */
data class Task(
    @DocumentId
    var id: String = "",
    var name: String = "",
    var isCompleted: Boolean = false,
    var timestamp: Timestamp? = null
)