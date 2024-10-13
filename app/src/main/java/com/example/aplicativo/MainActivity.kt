package com.example.aplicativo

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.aplicativo.data.db.AppDatabase
import com.example.aplicativo.data.db.dao.TarefaDao
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var tarefaDao: TarefaDao
    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializar o RecyclerView
        recyclerView = findViewById(R.id.recyclerViewTarefas)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Inicializar o banco de dados e DAO
        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "tarefasDB").build()
        tarefaDao = db.tarefaDao()

        // Carregar tarefas do banco de dados de forma assíncrona
        atualizarTarefas()


        // Navegar para a tela de adicionar nova tarefa
        val fabAddTarefa = findViewById<FloatingActionButton>(R.id.fabAddTarefa)
        fabAddTarefa.setOnClickListener {
            val intent = Intent(this, AdicionarTarefaActivity::class.java)
            startActivity(intent)
        }
    }

    // Método para atualizar a lista de tarefas
    private fun atualizarTarefas() {
        lifecycleScope.launch {
            val tarefas = tarefaDao.listarTarefas()  // Busca todas as tarefas do banco
            recyclerView.adapter = TarefaAdapter(tarefas)  // Atualiza o adapter do RecyclerView
        }
    }


    // Chamar atualizarTarefas() quando a atividade voltar a ser visível
    override fun onResume() {
        super.onResume()
        atualizarTarefas()  // Atualiza a lista de tarefas sempre que a atividade é retomada
    }
}