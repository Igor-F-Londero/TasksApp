package com.example.taskapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.HapticFeedbackConstants
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.commit
import com.example.taskapp.domain.model.Task
import com.example.taskapp.ui.task.NewTaskActivity
import com.example.taskapp.ui.task.TaskListFragment
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private lateinit var taskListFragment: TaskListFragment

    // Launcher atualizado para receber os dados dos novos Chips e Inputs
    private val newTaskLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data     = result.data ?: return@registerForActivityResult
            val title    = data.getStringExtra("task_title") ?: return@registerForActivityResult
            val desc     = data.getStringExtra("task_desc") ?: ""

            // Agora o app está preparado para receber a prioridade vinda dos Chips
            val priority = data.getStringExtra("task_priority") ?: "Média"

            taskListFragment.addTask(Task(title = title, description = desc, priority = priority))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvGreeting: TextView = findViewById(R.id.tvGreeting)
        tvGreeting.text = getGreeting()

        // Inicialização do Fragment
        if (savedInstanceState == null) {
            taskListFragment = TaskListFragment()
            supportFragmentManager.commit {
                replace(R.id.fragmentContainer, taskListFragment)
            }
        } else {
            taskListFragment = supportFragmentManager
                .findFragmentById(R.id.fragmentContainer) as TaskListFragment
        }

        // Configuração do FAB (Botão flutuante)
        val fabAdd: FloatingActionButton = findViewById(R.id.fabAdd)
        fabAdd.setOnClickListener {
            // Feedback tátil ao abrir nova tarefa (combina com o estilo moderno)
            it.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY)

            newTaskLauncher.launch(Intent(this, NewTaskActivity::class.java))
        }
    }

    /**
     * Função de saudação personalizada
     */
    private fun getGreeting(): String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

        return when (hour) {
            in 5..11 -> "               Bom dia, Igor!"
            in 12..17 -> "              Boa tarde, Igor!"
            in 18..23 -> "              Boa noite, Igor!"
            else -> "                     Boa madrugada, Igor!"
        }
    }
}