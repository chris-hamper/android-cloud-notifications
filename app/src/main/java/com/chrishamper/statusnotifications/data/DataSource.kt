package com.chrishamper.statusnotifications.data;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

class DataSource(messages: List<Message>) {
    private val liveData = MutableLiveData(messages)

    fun addMessage(msg: Message) {
        val currentList = liveData.value
        if (currentList == null) {
            liveData.postValue(listOf(msg))
        }
        else {
            val updatedList = currentList.toMutableList()
            updatedList.add(0, msg)
            liveData.postValue(updatedList)
        }
    }

    fun getMessageList(): LiveData<List<Message>> {
        return liveData
    }

    companion object {
        private var INSTANCE: DataSource? = null

        fun getDataSource(messages: List<Message>): DataSource {
            return synchronized(DataSource::class) {
                val newInstance = INSTANCE ?: DataSource(messages)
                INSTANCE = newInstance
                newInstance
            }
        }
    }
}
