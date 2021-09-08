package com.chrishamper.statusnotifications.messageList

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chrishamper.statusnotifications.data.DataSource
import com.chrishamper.statusnotifications.data.Message
import java.util.*

class MessageListViewModel(val dataSource: DataSource) : ViewModel() {
    val liveData = dataSource.getMessageList()

    fun insertMessage(title: String, body: String, received: Date) {
        dataSource.addMessage(Message(title, body, received))
    }
}

class MessageListViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MessageListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MessageListViewModel(
                dataSource = DataSource.getDataSource(mutableListOf(Message("Title", "Body", Calendar.getInstance().time)))
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}