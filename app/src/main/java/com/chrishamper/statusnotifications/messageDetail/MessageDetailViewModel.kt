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
import com.chrishamper.statusnotifications.data.DataSource
import com.chrishamper.statusnotifications.data.Message

class MessageDetailViewModel(private val datasource: DataSource) : ViewModel() {
    /* Queries datasource to returns a message that corresponds to an id. */
    fun getMessageForId(id: String) : Message? {
        return datasource.getMessageForId(id)
    }

    /* Tells datasource to remove a message. */
    fun removeMessage(message: Message) {
        datasource.removeMessage(message)
    }
}

class MessageDetailViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MessageDetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MessageDetailViewModel(
                datasource = DataSource.getDataSource()
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}