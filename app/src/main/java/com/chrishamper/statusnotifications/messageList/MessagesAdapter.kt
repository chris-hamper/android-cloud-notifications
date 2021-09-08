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

class MessagesAdapter(private val onClick: (Message) -> Unit)
    : ListAdapter<Message, MessagesAdapter.MessageViewHolder>(MessageDiffCallback) {

    class MessageViewHolder(itemView: View, val onClick: (Message) -> Unit) :
        RecyclerView.ViewHolder(itemView) {

        private val titleTextView: TextView = itemView.findViewById(R.id.message_title)
        private val bodyTextView: TextView = itemView.findViewById(R.id.message_body)
        private var currentMessage: Message? = null

        init {
            itemView.setOnClickListener {
                currentMessage?.let {
                    onClick(it)
                }
            }
        }

        fun bind(msg: Message) {
            currentMessage = msg

            titleTextView.text = msg.title
            bodyTextView.text = msg.body
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.message_item, parent, false)
        return MessageViewHolder(view, onClick)
    }

    /* Gets current flower and uses it to bind view. */
    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val msg = getItem(position)
        holder.bind(msg)

    }
}

object MessageDiffCallback : DiffUtil.ItemCallback<Message>() {
    override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem.received == newItem.received
    }
}