package com.chrishamper.statusnotifications.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {
    @Query("SELECT * FROM messages ORDER BY sent DESC")
    fun getAllBySent(): Flow<List<Message>>

//    @Query("SELECT * FROM messages WHERE id = :id LIMIT 1")
//    suspend fun getByID(id: String): Flow<Message>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(msg: Message)

    @Delete
    suspend fun delete(msg: Message)
}