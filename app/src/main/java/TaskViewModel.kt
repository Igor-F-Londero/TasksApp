import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.taskapp.data.repository.TaskRepository
import com.example.taskapp.domain.model.Task
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class TaskViewModel(private val repository: TaskRepository) : ViewModel() {

    // 1. Pega todas as tarefas do repositório
    private val allTasks = repository.allTasks

    private val _filterPriority = MutableStateFlow("Todas")

    val filteredTasks: StateFlow<List<Task>> = allTasks
        .combine(_filterPriority) { tasks, priority ->
            if (priority == "Todas") {
                tasks
            } else {
                tasks.filter { it.priority.equals(priority, ignoreCase = true) }
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // 4. Função que o Fragment chama ao clicar no Chip
    fun setFilter(priority: String) {
        _filterPriority.value = priority
    }

    // Funções de manipulação de dados
    fun insert(task: Task) = viewModelScope.launch { repository.insert(task) }
    fun update(task: Task) = viewModelScope.launch { repository.update(task) }
    fun delete(task: Task) = viewModelScope.launch { repository.delete(task) }
}