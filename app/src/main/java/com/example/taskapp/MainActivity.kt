package com.example.taskapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
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

    private val newTaskLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data     = result.data ?: return@registerForActivityResult
            val title    = data.getStringExtra("task_title") ?: return@registerForActivityResult
            val desc     = data.getStringExtra("task_desc") ?: ""
            val priority = data.getStringExtra("task_priority") ?: "Média"
            taskListFragment.addTask(Task(title = title, description = desc, priority = priority))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val tvGreeting: TextView = findViewById(R.id.tvGreeting)
        tvGreeting.text = getGreeting()

        // Só cria o Fragment se não existir ainda
        if (savedInstanceState == null) {
            taskListFragment = TaskListFragment()
            supportFragmentManager.commit {
                replace(R.id.fragmentContainer, taskListFragment)
            }
        } else {
            // Recupera o Fragment já existente
            taskListFragment = supportFragmentManager
                .findFragmentById(R.id.fragmentContainer) as TaskListFragment
        }

        findViewById<FloatingActionButton>(R.id.fabAdd).setOnClickListener {
            newTaskLauncher.launch(Intent(this, NewTaskActivity::class.java))
        }
    }
    private fun getGreeting(): String {
        val hour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)

        return when (hour) {
            in 5..11 -> ", Bom dia!"
            in 12..17 -> ", Boa tarde!"
            in 18..23 -> ", Boa noite!"
            else -> ", Boa madrugada!" // Entre 00h e 04h
        }
    }
}
