package com.example.taskapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.RadioGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.textfield.TextInputEditText

class NewTaskActivity : AppCompatActivity() {

    // Modo edição: se vier um taskId pelo Intent, estamos editando
    private var editingTaskId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_task)

        val etTitle  = findViewById<TextInputEditText>(R.id.etTaskTitle)
        val etDesc   = findViewById<TextInputEditText>(R.id.etTaskDesc)
        val rgPriority = findViewById<RadioGroup>(R.id.rgPriority)
        val btnSave  = findViewById<Button>(R.id.btnSaveTask)
        val tvError  = findViewById<TextView>(R.id.tvError)

        // Verifica se chegou dados para edição
        val extras = intent.extras
        if (extras != null && extras.getInt("task_id",-1)!= -1) {
            editingTaskId        = extras.getInt("task_id", -1)
            etTitle.setText(extras.getString("task_title", ""))
            etDesc.setText(extras.getString("task_desc", ""))

            // Restaura a prioridade selecionada
            when (extras.getString("task_priority", "Média")) {
                "Baixa" -> rgPriority.check(R.id.rbLow)
                "Alta"  -> rgPriority.check(R.id.rbHigh)
                else    -> rgPriority.check(R.id.rbMedium)
            }

            // Muda o título da tela para "Editar tarefa"
            supportActionBar?.title = getString(R.string.title_edit_task)
            btnSave.text = getString(R.string.btn_update_task)
        }

        btnSave.setOnClickListener {
            val title = etTitle.text.toString().trim()
            val desc  = etDesc.text.toString().trim()

            // Validação: título obrigatório
            if (title.isEmpty()) {
                tvError.visibility = View.VISIBLE
                return@setOnClickListener
            }
            tvError.visibility = View.GONE

            // Lê a prioridade selecionada
            val priority = when (rgPriority.checkedRadioButtonId) {
                R.id.rbLow  -> "Baixa"
                R.id.rbHigh -> "Alta"
                else        -> "Média"
            }

            // Devolve os dados para a MainActivity via Intent
            val resultIntent = Intent()
            resultIntent.putExtra("task_id",       editingTaskId)
            resultIntent.putExtra("task_title",    title)
            resultIntent.putExtra("task_desc",     desc)
            resultIntent.putExtra("task_priority", priority)

            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }
}