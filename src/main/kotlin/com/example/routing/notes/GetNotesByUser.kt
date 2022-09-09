package com.example.routing.notes

import com.example.db.Database
import com.example.entities.NoteEntity
import com.example.model.notes.Note
import com.example.model.notes.NoteResponse
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.ktorm.dsl.*

fun Routing.getNotesByUser(){

    val db = Database.connect

    authenticate {
        get("notes/getNotes/{id}"){
            val userId = call.parameters["id"]?.toIntOrNull()
            if(userId != null){
                db.from(NoteEntity).select().where {
                    NoteEntity.userId eq userId
                }.map {
                    val note = it[NoteEntity.note]!!
                    val userIdFromNote = it[NoteEntity.userId]!!
                    val noteId = it[NoteEntity.id]!!
                    Note(
                        id = noteId,
                        note = note,
                        userId = userIdFromNote
                    )
                }.let {
                    if(it.isNotEmpty()){
                        call.respond(
                            HttpStatusCode.OK,
                            NoteResponse(
                                true,
                                "notes",
                                it
                            )
                        )
                    }else{
                        call.respond(
                            HttpStatusCode.BadRequest,
                            NoteResponse(
                                false,
                                "Failed to get notes",
                                null
                            )
                        )
                    }
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