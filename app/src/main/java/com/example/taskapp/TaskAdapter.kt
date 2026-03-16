package com.example.taskapp

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(
    private val tasks: MutableList<Task>,
    private val onItemClick: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    // ViewHolder: segura as referências das views de cada item
    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView       = itemView.findViewById(R.id.tvTitle)
        val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        val cbDone: CheckBox        = itemView.findViewById(R.id.cbDone)
    }

    // Cria o ViewHolder inflando o layout item_task.xml
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    // Preenche cada item com os dados da tarefa
    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]

        holder.tvTitle.text       = task.title
        holder.tvDescription.text = task.description
        // Marca/desmarca o checkbox de acordo com o status da tarefa
        holder.cbDone.setOnCheckedChangeListener (null)
        holder.cbDone.isChecked   = task.isDone

        // Risco no texto quando concluída
        if (task.isDone) {
            holder.tvTitle.paintFlags =
                holder.tvTitle.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            holder.tvTitle.paintFlags =
                holder.tvTitle.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }


        // Clique no checkbox → marca/desmarca
        holder.cbDone.setOnCheckedChangeListener { _, isChecked ->
            task.isDone = isChecked
            notifyItemChanged(position)
        }

        // Clique no item → abre detalhes
        holder.itemView.setOnClickListener {
            onItemClick(task)
        }
    }

    override fun getItemCount(): Int = tasks.size

    // Adiciona nova tarefa e atualiza a lista
    fun addTask(task: Task) {
        tasks.add(task)
        notifyItemInserted(tasks.size - 1)
    }
}