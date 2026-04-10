package com.example.taskapp.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Representa uma tarefa no sistema.
 * 
 * @property id Identificador único gerado automaticamente pelo Room.
 * @property title O título ou resumo da tarefa.
 * @property description Detalhes adicionais sobre o que deve ser feito.
 * @property isDone Estado de conclusão da tarefa.
 * @property priority Nível de urgência: Alta, Média ou Baixa.
 */
@Entity(tableName = "tasks")
data class Task(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val title: String,
    val description: String,
    var isDone: Boolean = false,
    val priority: String = "Média"
)
