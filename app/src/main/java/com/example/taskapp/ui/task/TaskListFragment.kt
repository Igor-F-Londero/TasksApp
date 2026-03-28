package com.example.taskapp.ui.task

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.taskapp.R
import com.example.taskapp.data.local.AppDatabase
import com.example.taskapp.data.repository.TaskRepository
import com.example.taskapp.domain.model.Task
import com.example.taskapp.ui.task.viewmodel.TaskViewModel
import com.example.taskapp.ui.task.viewmodel.TaskViewModelFactory
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import kotlinx.coroutines.launch

class TaskListFragment : Fragment() {

    private lateinit var adapter: TaskAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var layoutEmpty: LinearLayout
    private lateinit var viewModel: TaskViewModel

    private val detailLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data = result.data ?: return@registerForActivityResult

            val task = Task(
                id = data.getIntExtra("task_id", -1),
                title = data.getStringExtra("task_title") ?: "",
                description = data.getStringExtra("task_desc") ?: "",
                priority = data.getStringExtra("task_priority") ?: "Média",
                isDone = data.getBooleanExtra("task_is_done", false)
            )

            if (data.getBooleanExtra("deleted", false)) {
                viewModel.delete(task)
            } else {
                viewModel.update(task)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_task_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 1. Inicializa Banco e ViewModel
        val db = AppDatabase.getDatabase(requireContext())
        val repository = TaskRepository(db.taskDao())
        val factory = TaskViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory)[TaskViewModel::class.java]

        // 2. Mapeia as Views
        recyclerView = view.findViewById(R.id.recyclerView)
        layoutEmpty  = view.findViewById(R.id.layoutEmpty)
        // CORREÇÃO: Usando o ID chipGroupFilter que está no XML
        val chipGroup = view.findViewById<ChipGroup>(R.id.chipGroupFilter)

        // 3. Configura o Adapter
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
                viewModel.update(task)
            }
        )

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // 4. Configura o Filtro por Chips
        chipGroup?.setOnCheckedStateChangeListener { group, checkedIds ->
            if (checkedIds.isNotEmpty()) {
                val chip = group.findViewById<Chip>(checkedIds.first())
                viewModel.setFilter(chip.text.toString())
            } else {
                viewModel.setFilter("Todas")
            }
        }

        // 5. Observa os dados (filtrados) do ViewModel
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.filteredTasks.collect { tasks ->
                adapter.updateList(tasks)
                layoutEmpty.visibility = if (tasks.isEmpty()) View.VISIBLE else View.GONE
                recyclerView.visibility = if (tasks.isEmpty()) View.GONE else View.VISIBLE
            }
        }
    }

    fun addTask(task: Task) {
        viewModel.insert(task)
    }
}
