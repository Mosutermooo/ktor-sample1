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

fun Routing.deleteNote(){

    val db = Database.connect

    authenticate {
        delete ("/note/delete/{id}") {
            val id = call.parameters["id"]?.toIntOrNull()

            val note = db.from(NoteEntity).select().where {
                NoteEntity.id eq id!!
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

            if (note != null){
                if(id != null){
                    val deleteNote = db.delete(NoteEntity){
                        it.id eq id
                    }
                    if(deleteNote == 1){
                        call.respond(
                            HttpStatusCode.OK,
                            NoteResponse(
                                true,
                                "Note Successfully deleted"
                            )
                        )
                    }else{
                        call.respond(
                            HttpStatusCode.BadRequest,
                            NoteResponse(
                                false,
                                "Something went wrong deleting the note"
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
            }else{
                call.respond(
                    HttpStatusCode.OK,
                    NoteResponse(
                        false,
                        "Note Doesnt Exist"
                    )
                )
            }
        }
    }
}