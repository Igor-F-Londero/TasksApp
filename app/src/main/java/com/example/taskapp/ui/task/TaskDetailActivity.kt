package com.example.taskapp.ui.task

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.taskapp.R
import com.google.android.material.checkbox.MaterialCheckBox

class TaskDetailActivity : AppCompatActivity() {

    private var taskId: Int = -1
    private var isDeleted: Boolean = false


    private val editLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data ?: return@registerForActivityResult

            val updatedTitle = data.getStringExtra("task_title") ?: ""
            val updatedDesc = data.getStringExtra("task_desc") ?: ""
            val updatedPriority = data.getStringExtra("task_priority") ?: "Média"

            findViewById<TextView>(R.id.tvDetailTitle).text = updatedTitle
            findViewById<TextView>(R.id.tvDetailDesc).text = updatedDesc

            val tvPriority = findViewById<TextView>(R.id.tvPriorityBadge)
            tvPriority.text = updatedPriority
            setupPriorityBadge(tvPriority, updatedPriority)

            sendResultBack()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)

        val tvTitle: TextView = findViewById(R.id.tvDetailTitle)
        val tvDesc: TextView = findViewById(R.id.tvDetailDesc)
        val tvPriority: TextView = findViewById(R.id.tvPriorityBadge)
        val cbDone: MaterialCheckBox = findViewById(R.id.cbDetailDone) // Atualizado para MaterialCheckBox
        val btnDelete: Button = findViewById(R.id.btnDelete)
        val btnEdit: Button = findViewById(R.id.btnEdit)

        taskId = intent.getIntExtra("task_id", -1)
        val title = intent.getStringExtra("task_title") ?: ""
        val desc = intent.getStringExtra("task_desc") ?: ""
        val priority = intent.getStringExtra("task_priority") ?: "Média"
        val isDone = intent.getBooleanExtra("task_is_done", false)

        tvTitle.text = title
        tvDesc.text = desc
        tvPriority.text = priority
        cbDone.isChecked = isDone

        setupPriorityBadge(tvPriority, priority)
        updateTaskAlpha(isDone, tvTitle, tvDesc)

        cbDone.setOnCheckedChangeListener { _, isChecked ->
            updateTaskAlpha(isChecked, tvTitle, tvDesc)
            sendResultBack()
        }

        btnDelete.setOnClickListener {
            isDeleted = true
            finish()
        }

        btnEdit.setOnClickListener {
            val intent = Intent(this, NewTaskActivity::class.java).apply {
                putExtra("task_id", taskId)
                putExtra("task_title", tvTitle.text.toString())
                putExtra("task_desc", tvDesc.text.toString())
                putExtra("task_priority", tvPriority.text.toString())
            }
            editLauncher.launch(intent)
        }
    }

    private fun setupPriorityBadge(textView: TextView, priority: String) {
        val colorRes = when (priority) {
            "Alta" -> R.color.priority_high
            "Baixa" -> R.color.priority_low
            else -> R.color.purple_main
        }
        val color = ContextCompat.getColor(this, colorRes)
        textView.backgroundTintList = ColorStateList.valueOf(color)
    }

    private fun updateTaskAlpha(isDone: Boolean, title: TextView, desc: TextView) {
        val alpha = if (isDone) 0.5f else 1.0f
        title.animate().alpha(alpha).setDuration(300).start()
        desc.animate().alpha(alpha).setDuration(300).start()
    }

    private fun sendResultBack() {
        val tvTitle = findViewById<TextView>(R.id.tvDetailTitle) ?: return
        val cbDone: MaterialCheckBox = findViewById(R.id.cbDetailDone)
        val resultIntent = Intent().apply {
            putExtra("task_id", taskId)
            putExtra("task_title", findViewById<TextView>(R.id.tvDetailTitle).text.toString())
            putExtra("task_desc", findViewById<TextView>(R.id.tvDetailDesc).text.toString())
            putExtra("task_priority", findViewById<TextView>(R.id.tvPriorityBadge).text.toString())
            putExtra("task_is_done", cbDone.isChecked)
            putExtra("deleted", isDeleted)
        }
        setResult(Activity.RESULT_OK, resultIntent)
    }

    override fun finish() {
        sendResultBack()
        super.finish()
    }
}