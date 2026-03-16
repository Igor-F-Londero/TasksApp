package com.example.taskapp

data class Task(
    val id: Int,
    val title: String,
    val description: String,
    var isDone: Boolean = false
)