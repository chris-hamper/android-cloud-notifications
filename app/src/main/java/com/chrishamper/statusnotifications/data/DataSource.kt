/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.chrishamper.statusnotifications.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.util.*

/* Handles operations on liveData and holds details about it. */
class DataSource(messages: List<Message>) {
    private val liveData = MutableLiveData(messages)

    /* Adds message to liveData and posts value. */
    fun addMessage(message: Message) {
        val currentList = liveData.value
        if (currentList == null) {
            liveData.postValue(listOf(message))
        } else {
            val updatedList = currentList.toMutableList()
            updatedList.add(0, message)
            liveData.postValue(updatedList)
        }
    }

    /* Removes ,essage from liveData and posts value. */
    fun removeMessage(message: Message) {
        val currentList = liveData.value
        if (currentList != null) {
            val updatedList = currentList.toMutableList()
            updatedList.remove(message)
            liveData.postValue(updatedList)
        }
    }

    /* Returns message given an ID. */
    fun getMessageForId(id: String): Message? {
        liveData.value?.let { messages ->
            return messages.firstOrNull{ it.id == id }
        }
        return null
    }

    fun getMessageList(): LiveData<List<Message>> {
        return liveData
    }

    companion object {
        private var INSTANCE: DataSource? = null

        fun getDataSource(): DataSource {
            return synchronized(DataSource::class) {
                val newInstance = INSTANCE ?: DataSource(listOf(
                    Message(
                        id = "test",
                        title = "Title",
                        body = "Body",
                        sent = Calendar.getInstance().time,
                    ),
                ))
                INSTANCE = newInstance
                newInstance
            }
        }
    }
}