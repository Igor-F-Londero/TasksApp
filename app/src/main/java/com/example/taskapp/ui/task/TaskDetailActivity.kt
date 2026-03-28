package com.example.taskapp.ui.task

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.taskapp.R

class TaskDetailActivity : AppCompatActivity() {

    private var taskId: Int = -1
    private var isDeleted: Boolean = false

    // Launcher para receber os dados atualizados da NewTaskActivity
    private val editLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data ?: return@registerForActivityResult
            
            // Atualiza os campos da tela com os novos valores
            val updatedTitle = data.getStringExtra("task_title") ?: ""
            val updatedDesc = data.getStringExtra("task_desc") ?: ""
            val updatedPriority = data.getStringExtra("task_priority") ?: "Média"
            
            findViewById<TextView>(R.id.tvDetailTitle).text = updatedTitle
            findViewById<TextView>(R.id.tvDetailDesc).text = updatedDesc
            val tvPriority = findViewById<TextView>(R.id.tvPriorityBadge)
            tvPriority.text = updatedPriority
            setupPriorityBadge(tvPriority, updatedPriority)
            
            // Avisa a MainActivity/Fragment que houve mudança no banco
            sendResultBack()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)

        val tvTitle: TextView = findViewById(R.id.tvDetailTitle)
        val tvDesc: TextView = findViewById(R.id.tvDetailDesc)
        val tvPriority: TextView = findViewById(R.id.tvPriorityBadge)
        val cbDone: CheckBox = findViewById(R.id.cbDetailDone)
        val btnDelete: Button = findViewById(R.id.btnDelete)
        val btnEdit: Button = findViewById(R.id.btnEdit)

        taskId = intent.getIntExtra("task_id", -1)
        val title = intent.getStringExtra("task_title") ?: ""
        val desc = intent.getStringExtra("task_desc") ?: ""
        val priority = intent.getStringExtra("task_priority") ?: "Média"
        val isDone = intent.getBooleanExtra("task_is_done", false)

        // Preencher a UI
        tvTitle.text = title
        tvDesc.text = desc
        tvPriority.text = priority
        cbDone.isChecked = isDone

        // Estilizar o badge de prioridade
        setupPriorityBadge(tvPriority, priority)

        // Listener do Checkbox
        cbDone.setOnCheckedChangeListener { _, _ ->
            sendResultBack()
        }

        // Listener do Botão Excluir
        btnDelete.setOnClickListener {
            isDeleted = true
            sendResultBack()
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
        val color = when (priority) {
            "Alta" -> "#FF5252"
            "Média" -> "#FFAB40"
            else -> "#4CAF50"
        }
        textView.backgroundTintList = ColorStateList.valueOf(Color.parseColor(color))
        textView.setTextColor(Color.WHITE)
    }

    private fun sendResultBack() {
        val cbDone: CheckBox = findViewById(R.id.cbDetailDone)
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

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        sendResultBack()
        super.onBackPressed()
    }
}
