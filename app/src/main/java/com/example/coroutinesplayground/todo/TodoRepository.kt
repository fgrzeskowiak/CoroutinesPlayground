package com.example.coroutinesplayground.todo

import com.example.coroutinesplayground.db.Todo
import com.example.coroutinesplayground.db.TodoDao

class TodoRepository(private val dao: TodoDao) {

    suspend fun insertTodo(text: String): List<Todo> {
        val todo = Todo(text = text)
        dao.addItem(todo)
        return dao.items()
    }
}