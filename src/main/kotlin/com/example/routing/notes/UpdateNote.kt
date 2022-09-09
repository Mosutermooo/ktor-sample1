package com.example.routing.notes

import com.example.db.Database
import com.example.entities.NoteEntity
import com.example.model.notes.Note
import com.example.model.notes.NoteRequest
import com.example.model.notes.NoteResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.dsl.*

fun Routing.updateNote(){

    val db = Database.connect

    authenticate {
        put("note/update/{id}"){
            val noteId = call.parameters["id"]?.toIntOrNull()
            val noteUpdate = call.receive<NoteRequest>()

            if(noteId != null){

                val note = db.from(NoteEntity).select().where {
                    NoteEntity.id eq noteId
                }.map {
                    val noteID = it[NoteEntity.id]!!
                    val note = it[NoteEntity.note]!!
                    val userId = it[NoteEntity.userId]!!
                    Note(
                        noteID,
                        note,
                        userId
                    )
                }.firstOrNull()

                if(note != null){
                    db.update(NoteEntity){
                        set(it.note, noteUpdate.note)
                        where {
                            it.id eq noteId
                        }
                    }.let {
                        if(it == 1){
                            call.respond(
                                HttpStatusCode.OK,
                                NoteResponse(
                                    true,
                                    "Successfully updated note with noteId: ${note.id}, and userId: ${note.userId}"
                                )
                            )
                        }else{
                            call.respond(
                                HttpStatusCode.BadRequest,
                                NoteResponse(
                                    false,
                                    "Something went wrong updating note"
                                )
                            )
                        }
                    }

                }else{

                    call.respond(
                        HttpStatusCode.OK,
                        NoteResponse(
                            false,
                            "Note Doesnt Exist"
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