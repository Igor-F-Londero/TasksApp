package com.example.taskapp

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: TaskAdapter
    private val taskList = mutableListOf<Task>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        val fabAdd       = findViewById<FloatingActionButton>(R.id.fabAdd)

        // Configura o Adapter com callback de clique
        adapter = TaskAdapter(taskList) { task ->
            // Clique em um item → abre tela de detalhes (próximo passo)
        //    val intent = Intent(this, TaskDetailActivity::class.java)
          //  intent.putExtra("task_title", task.title)
          //  intent.putExtra("task_desc", task.description)
            startActivity(intent)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Dados de exemplo para testar
        adapter.addTask(Task(1, "Estudar Kotlin", "Revisar coroutines e flows"))
        adapter.addTask(Task(2, "Fazer o trabalho", "Implementar Room no app"))

        // Botão flutuante → abre tela de nova tarefa (próximo passo)
        fabAdd.setOnClickListener {
         //   val intent = Intent(this, NewTaskActivity::class.java)
            startActivity(intent)
        }
    }
}