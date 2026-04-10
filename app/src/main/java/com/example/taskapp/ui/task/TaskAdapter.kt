package com.example.taskapp.ui.task

import android.animation.ValueAnimator
import android.graphics.Paint
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StrikethroughSpan
import android.view.HapticFeedbackConstants
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

        holder.tvTitle.text = task.title
        holder.tvDescription.text = task.description

        // Remove o listener antes de setar o estado para evitar disparos infinitos no Scroll
        holder.cbDone.setOnCheckedChangeListener(null)
        holder.cbDone.isChecked = task.isDone

        // Aplica o estilo estático inicial (sem animação)
        applyInitialStyle(holder, task.isDone)

        holder.cbDone.setOnCheckedChangeListener { view, isChecked ->
            task.isDone = isChecked

            // 1. Feedback tátil (vibração curta de confirmação)
            view.performHapticFeedback(HapticFeedbackConstants.CONFIRM)

            // 2. Dispara a animação visual moderna
            animateTaskStatus(holder, isChecked)

            onTaskStatusChanged(task)
        }

        holder.itemView.setOnClickListener {
            onItemClick(task)
        }
    }

    /** Aplica o estilo inicial sem animação. */
    private fun applyInitialStyle(holder: TaskViewHolder, isDone: Boolean) {
        val textView = holder.tvTitle
        if (isDone) {
            val span = SpannableString(textView.text)
            span.setSpan(StrikethroughSpan(), 0, textView.text.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            textView.text = span
            holder.itemView.alpha = 0.5f
        } else {
            // Limpa spans e volta opacidade ao normal
            textView.text = textView.text.toString()
            holder.itemView.alpha = 1.0f
        }
    }

    /**
     * Executa a animação de risco (Strike-through) e opacidade.
     */
    private fun animateTaskStatus(holder: TaskViewHolder, isDone: Boolean) {
        val textView = holder.tvTitle
        val text = textView.text.toString()
        val span = SpannableString(text)
        val strikeSpan = StrikethroughSpan()

        if (isDone) {
            val animator = ValueAnimator.ofInt(0, text.length)
            animator.duration = 350
            animator.addUpdateListener { animation ->
                val endValue = animation.animatedValue as Int
                span.setSpan(strikeSpan, 0, endValue, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                textView.text = span
            }

            // Agora animamos o card inteiro para parecer que ficou mais opaco
            holder.itemView.animate().alpha(0.5f).setDuration(350).start()
            animator.start()
        } else {
            textView.text = text
            holder.itemView.animate().alpha(1.0f).setDuration(350).start()
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
