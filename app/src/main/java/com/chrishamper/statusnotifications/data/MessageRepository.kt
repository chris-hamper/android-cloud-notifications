package com.chrishamper.statusnotifications.data

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow

class MessageRepository(private val messageDao: MessageDao) {
    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allMessages: Flow<List<Message>> = messageDao.getAllOrderedBySent()

    fun getByID(id: String): LiveData<Message> {
        return messageDao.getByID(id)
    }

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(word: Message) {
        messageDao.insert(word)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun delete(word: Message) {
        messageDao.delete(word)
    }
}