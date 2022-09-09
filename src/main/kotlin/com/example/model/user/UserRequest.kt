package com.example.model.user

import kotlinx.serialization.Serializable
import org.mindrot.jbcrypt.BCrypt

@Serializable
data class UserRequest(
    val email: String,
    val username: String,
    val password: String
){
    fun hashedPassword() : String{
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    fun isValidCredentials(): Boolean{
        return password.length >= 4
    }



}