package com.example.model.user

import kotlinx.serialization.Serializable

@Serializable
data class User (
    val id: Int,
    val email: String,
    val username: String,
    val password: String
)