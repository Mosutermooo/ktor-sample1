package com.example.model.notes

import kotlinx.serialization.Serializable

@Serializable
data class Note (
    val id: Int,
    val note: String,
    val userId: Int? = null
        )