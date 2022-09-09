package com.example.routing.notes

import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.routing.*

fun Application.notesRoutes(){
    routing {
        addNoteByUserId()
        getNotesByUser()
        getNote()
        deleteNote()
        updateNote()
    }
}