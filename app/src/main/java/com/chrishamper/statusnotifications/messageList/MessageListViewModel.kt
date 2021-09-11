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

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.chrishamper.statusnotifications.data.DataSource
import com.chrishamper.statusnotifications.data.Message
import java.util.*
import kotlin.random.Random

class MessageListViewModel(val dataSource: DataSource) : ViewModel() {

    val liveData = dataSource.getMessageList()

    /* If the name and description are present, create new Message and add it to the datasource */
    fun insertMessage(id: String, title: String, body: String, sent: Long) {
        val msg = Message(id, title, body, Date(sent))

        dataSource.addMessage(msg)
    }
}

class MessageListViewModelFactory : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MessageListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MessageListViewModel(
                dataSource = DataSource.getDataSource()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}