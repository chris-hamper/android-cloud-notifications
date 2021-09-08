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

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.chrishamper.statusnotifications.R
import com.chrishamper.statusnotifications.data.Message

class MessageAdapter(private val onClick: (Message) -> Unit) :
    ListAdapter<Message, MessageAdapter.MessageViewHolder>(MessageDiffCallback) {

    /* ViewHolder for Message, takes in the inflated view and the onClick behavior. */
    class MessageViewHolder(itemView: View, val onClick: (Message) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val messageTitleView: TextView = itemView.findViewById(R.id.message_title)
        private val messageBodyView: TextView = itemView.findViewById(R.id.message_body)
        private var currentMessage: Message? = null

        init {
            itemView.setOnClickListener {
                currentMessage?.let {
                    onClick(it)
                }
            }
        }

        /* Bind message name and image. */
        fun bind(message: Message) {
            currentMessage = message

            messageTitleView.text = message.title
            messageBodyView.text = message.body
        }
    }

    /* Creates and inflates view and return MessageViewHolder. */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.message_item, parent, false)
        return MessageViewHolder(view, onClick)
    }

    /* Gets current message and uses it to bind view. */
    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

object MessageDiffCallback : DiffUtil.ItemCallback<Message>() {
    override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem.id == newItem.id
    }
}