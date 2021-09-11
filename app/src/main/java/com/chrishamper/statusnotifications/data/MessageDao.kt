package com.chrishamper.statusnotifications.data

import androidx.lifecycle.LiveData
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Query("SELECT * FROM messages ORDER BY sent DESC")
    fun getAllOrderedBySent(): Flow<List<Message>>

    @Query("SELECT * FROM messages WHERE id = :id LIMIT 1")
    fun getByID(id: String): LiveData<Message>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(msg: Message)

    @Delete
    suspend fun delete(msg: Message)
}