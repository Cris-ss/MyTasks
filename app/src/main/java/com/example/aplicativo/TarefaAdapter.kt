package com.example.aplicativo

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.aplicativo.data.db.Tarefa


class TarefaAdapter(private val tarefas: List<Tarefa>) : RecyclerView.Adapter<TarefaAdapter.TarefaViewHolder>() {

    class TarefaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titulo: TextView = itemView.findViewById(R.id.itemTitulo)
        val descricao: TextView = itemView.findViewById(R.id.itemDescricao)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TarefaViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_tarefa, parent, false)
        return TarefaViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: TarefaViewHolder, position: Int) {
        val tarefa = tarefas[position]
        holder.titulo.text = tarefa.titulo
        holder.descricao.text = tarefa.descricao

        // Clique na tarefa para ir para a tela de detalhes
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, DetalhesTarefaActivity::class.java).apply {
                putExtra("TAREFA_ID", tarefa.id)  // Passa o ID da tarefa
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount() = tarefas.size
}