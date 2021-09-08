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
import android.widget.Button
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.chrishamper.statusnotifications.R
import com.chrishamper.statusnotifications.messageList.MESSAGE_ID

class MessageDetailActivity : AppCompatActivity() {

    private val messageDetailViewModel by viewModels<MessageDetailViewModel> {
        MessageDetailViewModelFactory()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_message_detail)

        var currentMessageId: Long? = null

        /* Connect variables to UI elements. */
        val messageTitle: TextView = findViewById(R.id.message_detail_title)
        val messageBody: TextView = findViewById(R.id.message_detail_body)
        val removeMessageButton: Button = findViewById(R.id.remove_button)

        val bundle: Bundle? = intent.extras
        if (bundle != null) {
            currentMessageId = bundle.getLong(MESSAGE_ID)
        }

        // If currentMessageId is not null, get corresponding message and set fields
        currentMessageId?.let {
            val currentMessage = messageDetailViewModel.getMessageForId(it)
            messageTitle.text = currentMessage?.title
            messageBody.text = currentMessage?.body

            removeMessageButton.setOnClickListener {
                if (currentMessage != null) {
                    messageDetailViewModel.removeMessage(currentMessage)
                }
                finish()
            }
        }

    }
}