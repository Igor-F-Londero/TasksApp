package com.example.taskapp.data.repository

import com.example.taskapp.data.local.TaskDao
import com.example.taskapp.domain.model.Task
import kotlinx.coroutines.flow.Flow

/**
 * Repositório que abstrai a fonte de dados para o restante do aplicativo.
 * Segue o princípio de Single Source of Truth (Fonte única da verdade).
 */
class TaskRepository(private val taskDao: TaskDao) {

    /**
     * Retorna um Flow com a lista de todas as tarefas ordenadas por ID decrescente.
     * O Flow permite que a UI seja notificada automaticamente sempre que os dados mudarem.
     */
    val allTasks: Flow<List<Task>> = taskDao.getAllTasks()

    suspend fun insert(task: Task) = taskDao.insert(task)

    suspend fun update(task: Task) = taskDao.update(task)

    suspend fun delete(task: Task) = taskDao.delete(task)
}
