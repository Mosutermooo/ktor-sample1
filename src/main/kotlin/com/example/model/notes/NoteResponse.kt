package com.example.model.notes

import kotlinx.serialization.Serializable


@Serializable
data class NoteResponse (
    val success: Boolean,
    val message: String,
    val notes: List<Note>? = null
        )