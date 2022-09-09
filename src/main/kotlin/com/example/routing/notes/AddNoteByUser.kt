package com.example.routing.notes

import com.example.db.Database
import com.example.entities.NoteEntity
import com.example.model.notes.NoteRequest
import com.example.model.notes.NoteResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.dsl.*

fun Routing.addNoteByUserId(){

    val database = Database.connect

    authenticate {
        post("/note/addNote") {
            val body = call.receive<NoteRequest>()
            val note = body.note
            val userId = body.userId.toInt()

            val result = database.insert(NoteEntity){
                set(it.note, note)
                set(it.userId, userId)
            }

            if(result == 1){
                call.respond(
                    HttpStatusCode.OK,
                    NoteResponse(
                        true,
                        "Note added",
                        null
                    )
                )
            }else{
                call.respond(
                    HttpStatusCode.BadRequest,
                    NoteResponse(
                        false,
                        "Note failed to add",
                        null
                    )
                )
            }


        }
    }
}