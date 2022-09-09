package com.example.routing

import com.example.routing.auth.login
import com.example.routing.auth.register
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.authRoutes(){
    routing {
       register()
        login()

        authenticate {
            get ("/me"){
                val principle = call.principal<JWTPrincipal>()
                val username = principle!!.payload.getClaim("username").asString()
                val userId = principle.payload.getClaim("email").asString()
                call.respondText("Hello, $username with id: $userId")
            }
        }

    }

}

