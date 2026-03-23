package com.example.taskapp

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

class TaskDetailActivity : AppCompatActivity() {

    private var taskId: Int = -1
    private var taskTitle: String = ""
    private var taskDesc: String = ""
    private var taskPriority: String = "Média"
    private var taskIsDone: Boolean = false

    // Launcher para abrir a tela de edição e receber o resultado
    private val editLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data ?: return@registerForActivityResult

            taskTitle    = data.getStringExtra("task_title") ?: taskTitle
            taskDesc     = data.getStringExtra("task_desc") ?: taskDesc
            taskPriority = data.getStringExtra("task_priority") ?: taskPriority

            // Atualiza a tela com os novos dados
            updateUI()

            // Avisa a MainActivity que houve edição
            val resultIntent = Intent()
            resultIntent.putExtra("task_id",       taskId)
            resultIntent.putExtra("task_title",    taskTitle)
            resultIntent.putExtra("task_desc",     taskDesc)
            resultIntent.putExtra("task_priority", taskPriority)
            resultIntent.putExtra("task_is_done",  taskIsDone)
            setResult(Activity.RESULT_OK, resultIntent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_detail)

        // Recebe os dados da tarefa vinda da MainActivity
        taskId       = intent.getIntExtra("task_id", -1)
        taskTitle    = intent.getStringExtra("task_title") ?: ""
        taskDesc     = intent.getStringExtra("task_desc") ?: ""
        taskPriority = intent.getStringExtra("task_priority") ?: "Média"
        taskIsDone   = intent.getBooleanExtra("task_is_done", false)

        updateUI()

        findViewById<CheckBox>(R.id.cbDetailDone).setOnCheckedChangeListener { _, isChecked ->
            taskIsDone = isChecked
            // Propaga a mudança de status para a MainActivity
            val resultIntent = Intent()
            resultIntent.putExtra("task_id",      taskId)
            resultIntent.putExtra("task_title",   taskTitle)
            resultIntent.putExtra("task_desc",    taskDesc)
            resultIntent.putExtra("task_priority",taskPriority)
            resultIntent.putExtra("task_is_done", taskIsDone)
            resultIntent.putExtra("deleted",       true)
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }

        findViewById<Button>(R.id.btnEdit).setOnClickListener {
            val intent = Intent(this, NewTaskActivity::class.java).apply {
                putExtra("task_id",       taskId)
                putExtra("task_title",    taskTitle)
                putExtra("task_desc",     taskDesc)
                putExtra("task_priority", taskPriority)
            }
            editLauncher.launch(intent)
        }

        findViewById<Button>(R.id.btnDelete).setOnClickListener {
            // Confirmação antes de excluir
            AlertDialog.Builder(this)
                .setTitle(getString(R.string.dialog_delete_title))
                .setMessage(getString(R.string.dialog_delete_message))
                .setPositiveButton(getString(R.string.btn_confirm_delete)) { _, _ ->
                    val resultIntent = Intent()
                    resultIntent.putExtra("task_id", taskId)
                    resultIntent.putExtra("deleted", true)
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()
                }
                .setNegativeButton(getString(R.string.btn_cancel), null)
                .show()
        }
    }

    private fun updateUI() {
        findViewById<TextView>(R.id.tvDetailTitle).text = taskTitle
        findViewById<TextView>(R.id.tvDetailDesc).text =
            taskDesc.ifEmpty { getString(R.string.label_no_desc) }
        findViewById<CheckBox>(R.id.cbDetailDone).isChecked = taskIsDone

        val badge = findViewById<TextView>(R.id.tvPriorityBadge)
        badge.text = taskPriority
        badge.setTextColor(when (taskPriority) {
            "Alta"  -> Color.parseColor("#993C1D")
            "Baixa" -> Color.parseColor("#0F6E56")
            else    -> Color.parseColor("#854F0B")
        })
        badge.backgroundTintList = android.content.res.ColorStateList.valueOf(
            when (taskPriority) {
                "Alta"  -> Color.parseColor("#FAECE7")
                "Baixa" -> Color.parseColor("#E1F5EE")
                else    -> Color.parseColor("#FAEEDA")
            }
        )

        supportActionBar?.title = taskTitle
    }
}