package com.example.taskapp.ui.task

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.taskapp.R
import com.example.taskapp.domain.model.Task

class TaskAdapter(
    private var tasks: MutableList<Task>,
    private val onItemClick: (Task) -> Unit,
    private val onTaskStatusChanged: (Task) -> Unit
) : RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvDescription: TextView = itemView.findViewById(R.id.tvDescription)
        val cbDone: CheckBox = itemView.findViewById(R.id.cbDone)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        val task = tasks[position]

        holder.tvTitle.text       = task.title
        holder.tvDescription.text = task.description

        holder.cbDone.setOnCheckedChangeListener(null)
        holder.cbDone.isChecked = task.isDone

        updateTitleStyle(holder.tvTitle, task.isDone)

        holder.cbDone.setOnCheckedChangeListener { _, isChecked ->
            task.isDone = isChecked
            updateTitleStyle(holder.tvTitle, isChecked)
            onTaskStatusChanged(task)
        }

        holder.itemView.setOnClickListener {
            onItemClick(task)
        }
    }

    private fun updateTitleStyle(textView: TextView, isDone: Boolean) {
        if (isDone) {
            textView.paintFlags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
            textView.alpha = 0.5f
        } else {
            textView.paintFlags = textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
            textView.alpha = 1.0f
        }
    }

    override fun getItemCount(): Int = tasks.size

    fun updateList(newTasks: List<Task>) {
        this.tasks = newTasks.toMutableList()
        notifyDataSetChanged()
    }

    fun removeTask(id: Int) {
        val index = tasks.indexOfFirst { it.id == id }
        if (index != -1) {
            tasks.removeAt(index)
            notifyItemRemoved(index)
        }
    }
}
