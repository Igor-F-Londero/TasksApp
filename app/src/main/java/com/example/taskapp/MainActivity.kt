package com.example.taskapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskapp.data.AppDatabase
import com.example.taskapp.data.TaskRepository
import com.example.taskapp.model.Task
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: TaskAdapter
    private lateinit var repository: TaskRepository

    private val newTaskLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data     = result.data ?: return@registerForActivityResult
            val title    = data.getStringExtra("task_title") ?: return@registerForActivityResult
            val desc     = data.getStringExtra("task_desc") ?: ""
            val priority = data.getStringExtra("task_priority") ?: "Média"

            lifecycleScope.launch {
                repository.insert(Task(title = title, description = desc, priority = priority))
            }
        }
    }

    private val detailLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data ?: return@registerForActivityResult
            val id   = data.getIntExtra("task_id", -1)

            if (data.getBooleanExtra("deleted", false)) {
                lifecycleScope.launch {
                    val task = Task(
                        id          = id,
                        title       = data.getStringExtra("task_title") ?: "",
                        description = data.getStringExtra("task_desc") ?: "",
                        priority    = data.getStringExtra("task_priority") ?: "Média",
                        isDone      = data.getBooleanExtra("task_is_done", false)
                    )
                    repository.delete(task)
                }
            } else {
                lifecycleScope.launch {
                    val task = Task(
                        id          = id,
                        title       = data.getStringExtra("task_title") ?: return@launch,
                        description = data.getStringExtra("task_desc") ?: "",
                        priority    = data.getStringExtra("task_priority") ?: "Média",
                        isDone      = data.getBooleanExtra("task_is_done", false)
                    )
                    repository.update(task)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val db = AppDatabase.getDatabase(this)
        repository = TaskRepository(db.taskDao())

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val fabAdd       = findViewById<FloatingActionButton>(R.id.fabAdd)

        adapter = TaskAdapter(
            tasks = mutableListOf(),
            onItemClick = { task ->
                val intent = Intent(this, TaskDetailActivity::class.java).apply {
                    putExtra("task_id",       task.id)
                    putExtra("task_title",    task.title)
                    putExtra("task_desc",     task.description)
                    putExtra("task_priority", task.priority)
                    putExtra("task_is_done",  task.isDone)
                }
                detailLauncher.launch(intent)
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Observa o banco e atualiza a lista automaticamente
        lifecycleScope.launch {
            repository.allTasks.collect { tasks ->
                adapter.updateList(tasks.toMutableList())
            }
        }

        fabAdd.setOnClickListener {
            newTaskLauncher.launch(Intent(this, NewTaskActivity::class.java))
        }
    }
}