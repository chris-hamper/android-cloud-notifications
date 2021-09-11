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

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.chrishamper.statusnotifications.MyApplication
import com.chrishamper.statusnotifications.R
import com.chrishamper.statusnotifications.messageList.MESSAGE_ID

class MessageDetailActivity : AppCompatActivity() {
    private val messageTitle: TextView by lazy { findViewById(R.id.message_detail_title) }
    private val messageBody: TextView by lazy { findViewById(R.id.message_detail_body) }
    private val removeMessageButton: Button by lazy { findViewById(R.id.remove_button) }

    private val messageDetailViewModel by viewModels<MessageDetailViewModel> {
        MessageDetailViewModelFactory((application as MyApplication).repo)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_detail)

        var messageId: String? = null
        intent.extras?.let { messageId = it.getString(MESSAGE_ID) }

        if (messageId == null) {
            Log.e(TAG, "No message ID passed in extras")
            finish()
            return
        }

        messageDetailViewModel.repo.getByID(messageId!!).observe(this, { msg ->
            msg?.let {
                // Connect fields to UI elements.
                messageTitle.text = msg.title
                messageBody.text = msg.body

                removeMessageButton.setOnClickListener {
                    messageDetailViewModel.removeMessage(msg)
                    finish()
                }
            }
        })
    }

    companion object {
        private const val TAG = "MessageDetailActivity"
    }
}