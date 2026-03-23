package com.example.taskapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskapp.data.AppDatabase
import com.example.taskapp.data.TaskRepository
import com.example.taskapp.model.Task
import kotlinx.coroutines.launch

class TaskListFragment : Fragment() {

    private lateinit var adapter: TaskAdapter
    private lateinit var repository: TaskRepository
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutEmpty: LinearLayout

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
                    val title = data.getStringExtra("task_title") ?: return@launch
                    val task = Task(
                        id          = id,
                        title       = title,
                        description = data.getStringExtra("task_desc") ?: "",
                        priority    = data.getStringExtra("task_priority") ?: "Média",
                        isDone      = data.getBooleanExtra("task_is_done", false)
                    )
                    repository.update(task)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_task_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerView = view.findViewById(R.id.recyclerView)
        layoutEmpty  = view.findViewById(R.id.layoutEmpty)

        val db = AppDatabase.getDatabase(requireContext())
        repository = TaskRepository(db.taskDao())

        adapter = TaskAdapter(
            tasks = mutableListOf(),
            onItemClick = { task ->
                val intent = Intent(requireContext(), TaskDetailActivity::class.java).apply {
                    putExtra("task_id", task.id)
                    putExtra("task_title", task.title)
                    putExtra("task_desc", task.description)
                    putExtra("task_priority", task.priority)
                    putExtra("task_is_done", task.isDone)
                }
                detailLauncher.launch(intent)
            },
            onTaskStatusChanged = { task ->
                lifecycleScope.launch {
                    repository.update(task)
                }
            }
        )



        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        lifecycleScope.launch {
            repository.allTasks.collect { tasks ->
                adapter.updateList(tasks.toMutableList())
                if (tasks.isEmpty()) {
                    layoutEmpty.visibility  = View.VISIBLE
                    recyclerView.visibility = View.GONE
                } else {
                    layoutEmpty.visibility  = View.GONE
                    recyclerView.visibility = View.VISIBLE
                }
            }
        }
    }

    fun addTask(task: Task) {
        lifecycleScope.launch {
            repository.insert(task)
        }
    }
}