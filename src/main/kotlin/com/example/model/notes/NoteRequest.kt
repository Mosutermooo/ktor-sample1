package com.example.model.notes

import kotlinx.serialization.Serializable

@Serializable
data class NoteRequest(
    val note: String,
    val userId: String
)
