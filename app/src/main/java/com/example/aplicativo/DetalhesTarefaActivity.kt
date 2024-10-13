package com.example.aplicativo

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import com.example.aplicativo.data.db.AppDatabase
import com.example.aplicativo.data.db.dao.TarefaDao
import kotlinx.coroutines.launch

class DetalhesTarefaActivity : AppCompatActivity() {
    private lateinit var tarefaDao: TarefaDao
    private var tarefaId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detalhes_tarefa)

        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "tarefasDB").build()
        tarefaDao = db.tarefaDao()

        // Pegar tarefa a partir do ID passado pela Intent
        tarefaId = intent.getIntExtra("TAREFA_ID", 0)
        carregarDetalhesTarefa()  // Chama o método para carregar os detalhes da tarefa

        // Botão para editar tarefa
        findViewById<Button>(R.id.btnEditarTarefa).setOnClickListener {
            val intent = Intent(this, AdicionarTarefaActivity::class.java)
            intent.putExtra("TAREFA_ID", tarefaId)
            startActivity(intent)
        }

        // Botão para excluir tarefa
        findViewById<Button>(R.id.btnExcluirTarefa).setOnClickListener {
            lifecycleScope.launch {
                val tarefa = tarefaDao.listarTarefas().find { it.id == tarefaId }
                tarefa?.let {
                    tarefaDao.deletar(it)
                    finish()  // Voltar para a tela anterior após excluir
                }
            }
        }
    }

    // Método para carregar os detalhes da tarefa
    private fun carregarDetalhesTarefa() {
        lifecycleScope.launch {
            val tarefa = tarefaDao.listarTarefas().find { it.id == tarefaId }

            tarefa?.let {
                findViewById<TextView>(R.id.textTituloDetalhe).text = it.titulo
                findViewById<TextView>(R.id.textDescricaoDetalhe).text = it.descricao
                findViewById<TextView>(R.id.textDataDetalhe).text = it.data
                findViewById<TextView>(R.id.textHoraDetalhe).text = it.horario
            }
        }
    }

    // Atualiza os dados sempre que a tela é retomada
    override fun onResume() {
        super.onResume()
        carregarDetalhesTarefa()  // Recarrega os detalhes da tarefa quando a atividade for retomada
    }
}