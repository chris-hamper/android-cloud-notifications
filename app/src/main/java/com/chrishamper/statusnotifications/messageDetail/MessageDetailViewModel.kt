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

package com.chrishamper.statusnotifications.messageDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.chrishamper.statusnotifications.data.Message
import com.chrishamper.statusnotifications.data.MessageRepository
import kotlinx.coroutines.launch

class MessageDetailViewModel(private val repo: MessageRepository) : ViewModel() {
    private val liveData = repo.allMessages.asLiveData()

    fun getMessageForId(id: String) : Message? {
        liveData.value?.let { messages ->
            return messages.firstOrNull { it.id == id }
        }
        return null
    }

    fun removeMessage(message: Message) = viewModelScope.launch {
        repo.delete(message)
    }
}

class MessageDetailViewModelFactory(private val repo: MessageRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MessageDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MessageDetailViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}