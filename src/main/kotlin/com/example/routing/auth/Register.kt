package com.example.routing.auth

import com.example.db.Database
import com.example.entities.UserEntity
import com.example.model.user.UserRequest
import com.example.model.user.UserResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.dsl.*
import java.util.*

fun Routing.register(){
    post("/register"){
        val user = call.receive<UserRequest>()
        val username = user.username.lowercase(Locale.getDefault())
        val email = user.email
        val password = user.hashedPassword()

        val userExists = Database.connect.from(UserEntity)
            .select()
            .where{ UserEntity.username eq username}
            .map { it[UserEntity.username] }.firstOrNull()

        if(userExists != null){
            call.respond(
                HttpStatusCode.NotAcceptable,
                UserResponse(
                    null,
                    false,
                    "Username already exists"
                )
            )
            return@post
        }

        if(!user.isValidCredentials()){
            call.respond(
                HttpStatusCode.BadRequest,
                UserResponse(
                    null,
                    false,
                    "Username or password are too small, please make the username length more than 3 and password more than 6"
                )
            )
            return@post
        }


        val adduser =  Database.connect.insert(UserEntity){
            set(it.username, username)
            set(it.email, email)
            set(it.password, password)
        }

        if(adduser == 1){
            call.respond(
                HttpStatusCode.OK,
                UserResponse(
                    null,
                    true,
                    "Successfully created account"
                )
            )
        }else{
            call.respond(
                HttpStatusCode.BadRequest,
                UserResponse(
                    null,
                    false,
                    "Something went wrong"
                )
            )
        }
    }
}