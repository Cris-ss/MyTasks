package com.example.aplicativo.data.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.aplicativo.data.db.Tarefa

@Dao
interface TarefaDao {

    @Insert
    suspend fun inserir(tarefa: Tarefa)

    @Query("SELECT * FROM tarefas")
    suspend fun listarTarefas(): List<Tarefa>

    @Update
    suspend fun atualizar(tarefa: Tarefa)

    @Delete
    suspend fun deletar(tarefa: Tarefa)
}
