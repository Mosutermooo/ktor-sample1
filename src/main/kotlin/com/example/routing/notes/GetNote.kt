package com.example.routing.notes

import com.example.db.Database.connect
import com.example.entities.NoteEntity
import com.example.model.notes.Note
import com.example.model.notes.NoteResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.dsl.*

fun Routing.getNote(){
    authenticate {
        get("/notes/getNote/{id}") {
            val noteId = call.parameters["id"]?.toIntOrNull()
            if(noteId != null){
               val note = connect.from(NoteEntity).select().where {
                    NoteEntity.id eq noteId
                }.map {
                    val id = it[NoteEntity.id]!!
                    val noteText = it[NoteEntity.note]!!
                    val userId = it[NoteEntity.userId]
                   Note(
                       id, noteText, userId
                   )
               }
                if(note.isNotEmpty()){
                    call.respond(
                        HttpStatusCode.OK,
                        NoteResponse(
                            true, "Single note",
                            note
                        )
                    )
                }else{
                    call.respond(
                        HttpStatusCode.BadRequest,
                        NoteResponse(
                            false, "Failed to get a single note",
                            null
                        )
                    )
                }
            }else{
                call.respond(
                    HttpStatusCode.NotAcceptable,
                    NoteResponse(
                        false, "Please enter a number",
                        null
                    )
                )
            }
        }
    }
}