package com.example.aplicativo

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.room.Room
import androidx.work.Data
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.aplicativo.data.db.AppDatabase
import com.example.aplicativo.data.db.Tarefa
import com.example.aplicativo.data.db.dao.TarefaDao
import kotlinx.coroutines.launch
import java.util.Calendar
import java.util.concurrent.TimeUnit

class AdicionarTarefaActivity : AppCompatActivity() {
    private lateinit var tarefaDao: TarefaDao
    private var tarefaId: Int = -1
    private var tarefaExistente: Tarefa? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adicionar_tarefa)

        val db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "tarefasDB")
            .addMigrations(AppDatabase.MIGRATION_1_2) // Adiciona a migração
            .build()
        tarefaDao = db.tarefaDao()

        tarefaId = intent.getIntExtra("TAREFA_ID", -1)
        val editData = findViewById<EditText>(R.id.editData)
        val editHora = findViewById<EditText>(R.id.editHora)

        // Adicionar TextWatcher para o EditText de data
        editData.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null && s.length == 2 && before == 0) {
                    editData.setText(getString(R.string.data_format, s))
                    editData.setSelection(editData.text.length)  // Move o cursor para o final
                }
                if (s != null && s.length == 5 && before == 0) {
                    editData.setText(getString(R.string.data_format, s))
                    editData.setSelection(editData.text.length)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // TextWatcher para o EditText de hora
        editHora.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (s != null && s.length == 2 && before == 0) {
                    editHora.setText(getString(R.string.hora_format, s))
                    editHora.setSelection(editHora.text.length)  // Move o cursor para o final
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Carregar dados existentes se for uma tarefa existente
        if (tarefaId != -1) {
            lifecycleScope.launch {
                tarefaExistente = tarefaDao.listarTarefas().find { it.id == tarefaId }
                tarefaExistente?.let { tarefa ->
                    findViewById<EditText>(R.id.editTitulo).setText(tarefa.titulo)
                    findViewById<EditText>(R.id.editDescricao).setText(tarefa.descricao)
                    editData.setText(tarefa.data)
                    editHora.setText(tarefa.horario)
                }
            }
        }

        val btnSalvar = findViewById<Button>(R.id.btnSalvarTarefa)
        btnSalvar.setOnClickListener {
            val titulo = findViewById<EditText>(R.id.editTitulo).text.toString()
            val descricao = findViewById<EditText>(R.id.editDescricao).text.toString()
            val data = editData.text.toString()
            val horario = editHora.text.toString()

            if (titulo.isNotBlank() && descricao.isNotBlank() && horario.isNotBlank()) {
                lifecycleScope.launch {
                    if (tarefaId == -1) {
                        // Nova tarefa
                        val novaTarefa = Tarefa(
                            titulo = titulo,
                            descricao = descricao,
                            data = data,
                            horario = horario
                        )
                        tarefaDao.inserir(novaTarefa)

                        agendarNotificacao(titulo, descricao, data, horario)
                    } else {
                        // Atualiza a tarefa existente
                        tarefaExistente?.let { tarefa ->
                            tarefa.titulo = titulo
                            tarefa.descricao = descricao
                            tarefa.data = data
                            tarefa.horario = horario
                            tarefaDao.atualizar(tarefa)

                            // Cancelar a notificação anterior
                            cancelarNotificacao(tarefaId)

                            // Agendar a nova notificação
                            agendarNotificacao(titulo, descricao, data, horario)
                        }
                    }
                    finish() // Fecha a atividade após salvar
                }
            } else {
                Toast.makeText(this, "Preencha todos os campos", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun cancelarNotificacao(tarefaId: Int) {
        WorkManager.getInstance(applicationContext).cancelAllWorkByTag(tarefaId.toString())
    }

    // Função para agendar a notificação
    private fun agendarNotificacao(
        titulo: String,
        descricao: String,
        data: String,
        horario: String
    ) {
        // Converta a data e hora para um objeto Calendar
        val calendar = Calendar.getInstance()
        // Suponha que a data está no formato "dd/MM/yyyy" e horário no formato "HH:mm"
        val partsData = data.split("/")
        val partsHora = horario.split(":")
        calendar.set(Calendar.DAY_OF_MONTH, partsData[0].toInt())
        calendar.set(Calendar.MONTH, partsData[1].toInt() - 1) // O mês é 0-indexed
        calendar.set(Calendar.YEAR, partsData[2].toInt())
        calendar.set(Calendar.HOUR_OF_DAY, partsHora[0].toInt())
        calendar.set(Calendar.MINUTE, partsHora[1].toInt())
        calendar.set(Calendar.SECOND, 0)

        // Verifica se a data/hora já passou
        if (calendar.timeInMillis < System.currentTimeMillis()) {
            return
        }

        // Calcula o delay até a data/hora
        val delay = (calendar.timeInMillis - System.currentTimeMillis())

        // Cria a Data para passar para o Worker
        val dataWorker = Data.Builder()
            .putString("TITULO", titulo)
            .putString("DESCRICAO", descricao)
            .build()

        // Cria um WorkRequest
        val workRequest = OneTimeWorkRequestBuilder<NotificacaoWorker>()
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setInputData(dataWorker)
            .addTag(tarefaId.toString()) // Adiciona a tag para identificação
            .build()

        // Envia o WorkRequest
        WorkManager.getInstance(applicationContext).enqueue(workRequest)
    }
}
