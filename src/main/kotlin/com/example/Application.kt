package com.example


import io.ktor.server.application.*
import com.example.routing.*
import com.example.routing.notes.notesRoutes
import com.example.utils.TokenManager
import com.typesafe.config.ConfigFactory
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.config.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*

fun main(){
    val config = HoconApplicationConfig(ConfigFactory.load())

    embeddedServer(Netty, port = 8080){

        install(Authentication){
            jwt {
                verifier(TokenManager(config).verifyJWTToken())
                realm = config.property("realm").getString()
                validate {
                    if(it.payload.getClaim("username").asString().isNotEmpty()){
                        JWTPrincipal(it.payload)
                    }else{
                        null
                    }
                }
            }
        }

        install(ContentNegotiation){
            json()
        }


        authRoutes()
        notesRoutes()




    }.start(true)
}