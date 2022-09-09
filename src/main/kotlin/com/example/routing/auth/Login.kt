package com.example.routing.auth

import com.example.db.Database
import com.example.entities.UserEntity
import com.example.model.user.User
import com.example.model.user.UserRequest
import com.example.model.user.UserResponse
import com.example.utils.TokenManager
import com.typesafe.config.ConfigFactory
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.selects.whileSelect
import org.ktorm.dsl.*
import org.mindrot.jbcrypt.BCrypt
import java.util.*

fun Routing.login(){
    val tokenManager = TokenManager(HoconApplicationConfig(ConfigFactory.load()))
    post("/login"){
        val user = call.receive<UserRequest>()
        val usernameReq = user.username.lowercase()
        val emailReq = user.email.lowercase()
        val password = user.password

        val userExists = Database.connect.from(UserEntity)
            .select()
            .where{
                UserEntity.username eq usernameReq
            }.map {
               val id = it[UserEntity.id]!!
               val username = it[UserEntity.username]!!
               val email = it[UserEntity.email]!!
               val userPassword = it[UserEntity.password]!!
               User(
                   id,email,username,userPassword
               )
            }.firstOrNull()

        if(!user.isValidCredentials()){
            call.respond(
                HttpStatusCode.BadRequest,
                UserResponse(
                    null,
                    false,
                    "Username or password are too small"
                )
            )
            return@post
        }

        if(userExists == null){
            call.respond(
                HttpStatusCode.Unauthorized,
                UserResponse(
                    null,
                    false,
                    "Invalid credentials"
                )
            )
            return@post
        }

        val doesPasswordMatch = BCrypt.checkpw(password, userExists.password)
        if(!doesPasswordMatch){
            call.respond(
                HttpStatusCode.NotFound,
                UserResponse(
                    null,
                    false,
                    "Password doesnt match with our password in the database, try again"
                )
            )
            return@post
        }

        val token = tokenManager.generateJWTToken(userExists)
        call.respond(
            HttpStatusCode.OK,
            UserResponse(
                token,
                true,
                "Logged in successfully",
                User(
                    userExists.id,
                    userExists.email,
                    userExists.username,
                    null.toString()
                )
            )

        )





    }
}