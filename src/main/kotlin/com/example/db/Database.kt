package com.example.db

import org.ktorm.database.Database

object Database{
    val connect = Database.connect(
        url = "jdbc:mysql://localhost:9918/note_app",
        driver = "com.mysql.cj.jdbc.Driver",
        user = "root",
        password = "Slezenkovski22."
    )
}