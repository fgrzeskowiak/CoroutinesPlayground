package com.example.coroutinesplayground.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface TodoDao {
    @Insert
    suspend fun addItem(todo: Todo)

    @Query("select * from Todo")
    suspend fun items(): List<Todo>

    @Query("""
        update Todo set completed = 1
        where id = :id""")
    suspend fun markCompleted(id: Int)
}