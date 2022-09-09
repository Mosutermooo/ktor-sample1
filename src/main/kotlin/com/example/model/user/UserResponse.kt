package com.example.model.user

import kotlinx.serialization.Serializable

@Serializable
data class UserResponse(
    val token: String?,
    val success: Boolean,
    val message: String,
    val User: User? = null
)