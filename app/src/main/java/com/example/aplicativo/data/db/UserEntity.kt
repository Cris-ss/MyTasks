package com.example.aplicativo.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tarefas")
data class Tarefa(
    @PrimaryKey(autoGenerate = true) var id: Int = 0,
    var titulo: String,
    var descricao: String,
    var data: String,
    var horario: String // Novo campo para horário

)
