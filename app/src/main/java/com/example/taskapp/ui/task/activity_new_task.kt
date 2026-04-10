package com.example.taskapp.ui.task

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.taskapp.R
import com.google.android.material.chip.ChipGroup
import com.google.android.material.textfield.TextInputEditText

class NewTaskActivity : AppCompatActivity() {

    private var editingTaskId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_task)


        val etTitle = findViewById<TextInputEditText>(R.id.etTaskTitle)
        val etDesc = findViewById<TextInputEditText>(R.id.etTaskDesc)
        val chipGroupPriority = findViewById<ChipGroup>(R.id.cgPriority)
        val btnSave = findViewById<Button>(R.id.btnSaveTask)
        val tvError = findViewById<TextView>(R.id.tvError)


        val extras = intent.extras
        if (extras != null && extras.getInt("task_id", -1) != -1) {
            editingTaskId = extras.getInt("task_id", -1)
            etTitle.setText(extras.getString("task_title", ""))
            etDesc.setText(extras.getString("task_desc", ""))


            val priority = extras.getString("task_priority", "Média")
            when (priority) {
                "Baixa" -> chipGroupPriority.check(R.id.chipLow)
                "Alta" -> chipGroupPriority.check(R.id.chipHigh)
                else -> chipGroupPriority.check(R.id.chipMedium)
            }

            supportActionBar?.title = "Editar tarefa"
            btnSave.text = "Atualizar tarefa"
        }

        btnSave.setOnClickListener {
            val title = etTitle.text.toString()
            val desc = etDesc.text.toString()


            val selectedPriority = when (chipGroupPriority.checkedChipId) {
                R.id.chipLow -> "Baixa"
                R.id.chipHigh -> "Alta"
                else -> "Média"
            }

            if (title.isNotEmpty()) {
                val resultIntent = Intent().apply {
                    putExtra("task_title", title)
                    putExtra("task_desc", desc)
                    putExtra("task_priority", selectedPriority)
                    if (editingTaskId != -1) putExtra("task_id", editingTaskId)
                }
                setResult(Activity.RESULT_OK, resultIntent)
                finish()
            } else {
                tvError.visibility = View.VISIBLE
            }
        }
    }
}