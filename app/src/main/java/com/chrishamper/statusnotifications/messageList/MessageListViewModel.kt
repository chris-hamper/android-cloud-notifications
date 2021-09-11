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

package com.chrishamper.statusnotifications.messageList

import androidx.lifecycle.*
import com.chrishamper.statusnotifications.data.Message
import com.chrishamper.statusnotifications.data.MessageRepository
import kotlinx.coroutines.launch
import java.util.*

class MessageListViewModel(private val repo: MessageRepository) : ViewModel() {
    val liveData: LiveData<List<Message>> = repo.allMessages.asLiveData()

    fun insertMessage(id: String, title: String, body: String, sent: Long) = viewModelScope.launch {
        repo.insert(Message(id, title, body, Date(sent)))
    }
}

class MessageListViewModelFactory(private val repo: MessageRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MessageListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MessageListViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}