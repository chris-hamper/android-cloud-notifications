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

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.chrishamper.statusnotifications.R
import com.chrishamper.statusnotifications.messageDetail.MessageDetailActivity
import com.chrishamper.statusnotifications.data.Message

const val MESSAGE_ID = "message id"
const val MESSAGE_TITLE = "title"
const val MESSAGE_BODY = "body"
const val NEW_MESSAGE_ACTIVITY_INTENT_CODE = 1

class MessageListActivity : AppCompatActivity() {
    private val newMessageActivityRequestCode = 1
    private val messageListViewModel by viewModels<MessageListViewModel> {
        MessageListViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val messageAdapter = MessageAdapter { msg -> adapterOnClick(msg) }

        val recyclerView: RecyclerView = findViewById(R.id.recycler_view)
        recyclerView.adapter = messageAdapter

        messageListViewModel.liveData.observe(this, {
            it?.let {
                messageAdapter.submitList(it as MutableList<Message>)
            }
        })
    }

    /* Opens MessageDetailActivity when RecyclerView item is clicked. */
    private fun adapterOnClick(message: Message) {
        val intent = Intent(this, MessageDetailActivity()::class.java)
        intent.putExtra(MESSAGE_ID, message.id)
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intentData: Intent?) {
        super.onActivityResult(requestCode, resultCode, intentData)

        /* Inserts message into viewModel. */
        if (requestCode == newMessageActivityRequestCode && resultCode == Activity.RESULT_OK) {
            intentData?.let { data ->
                val messageTitle = data.getStringExtra(MESSAGE_TITLE)
                val messageBody = data.getStringExtra(MESSAGE_BODY)

                messageListViewModel.insertMessage(messageTitle, messageBody)
            }
        }
    }
}