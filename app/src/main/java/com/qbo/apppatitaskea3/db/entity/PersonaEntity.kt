package com.qbo.apppatitaskea3.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "persona")
data class PersonaEntity(
    @PrimaryKey
    val id: Int,
    //@ColumnInfo(name = "Nombre")
    val nombres: String,
    val apellidos: String,
    val email: String,
    val usuario: String,
    val password: String,
    val esvoluntario: String
)
