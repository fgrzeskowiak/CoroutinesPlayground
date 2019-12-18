package com.example.coroutinesplayground.todo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.coroutinesplayground.db.Todo
import kotlinx.coroutines.launch

class TodoPresenter(private val repository: TodoRepository) : ViewModel() {

    private val todosLiveData = MutableLiveData<List<Todo>>()

    init {
        viewModelScope.launch {
            val newList = repository.insertTodo("dd")
            todosLiveData.postValue(newList)
        }
    }

    fun addItem(text: String) {
        viewModelScope.launch {
            repository.insertTodo(text)
        }
    }

    val todos: LiveData<List<Todo>> = todosLiveData

}