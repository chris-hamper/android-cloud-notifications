package com.chrishamper.statusnotifications

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.os.Build
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.chrishamper.statusnotifications.data.Message
import com.chrishamper.statusnotifications.messageList.MessageListViewModel
import com.chrishamper.statusnotifications.messageList.MessageListViewModelFactory
import com.chrishamper.statusnotifications.messageList.MessagesAdapter
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import java.util.Calendar

const val MESSAGE_TITLE = "title"
const val MESSAGE_BODY = "body"
const val NEW_MESSAGE_ACTIVITY_INTENT_CODE = 1

class MainActivity : AppCompatActivity() {
    private val messageListViewModel by viewModels<MessageListViewModel> {
        MessageListViewModelFactory(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val messagesAdapter = MessagesAdapter { msg -> adapterOnClick(msg) }

        val messageRecyclerView: RecyclerView = findViewById(R.id.message_list)
        messageRecyclerView.adapter = messagesAdapter

        messageListViewModel.liveData.observe(this, {
            it?.let {
                messagesAdapter.submitList(it as MutableList<Message>)
            }
        })

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create channel to show notifications.
            val channelId = getString(R.string.default_notification_channel_id)
            val channelName = getString(R.string.default_notification_channel_name)
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager?.createNotificationChannel(
                NotificationChannel(channelId,
                channelName, NotificationManager.IMPORTANCE_DEFAULT)
            )
        }

        // If a notification message is tapped, any data accompanying the notification
        // message is available in the intent extras. In this sample the launcher
        // intent is fired when the notification is tapped, so any accompanying data would
        // be handled here. If you want a different intent fired, set the click_action
        // field of the notification message to the desired intent. The launcher intent
        // is used when no click_action is specified.
        //
        // Handle possible data accompanying notification message.
        // [START handle_data_extras]
        intent.extras?.let {
            for (key in it.keySet()) {
                val value = intent.extras?.get(key)
                Log.d(TAG, "Key: $key Value: $value")
            }

            addMessageFromIntent(intent)
        }
        // [END handle_data_extras]

        Firebase.messaging.subscribeToTopic("status")
            .addOnCompleteListener { task ->
                var msg = getString(R.string.msg_subscribed)
                if (!task.isSuccessful) {
                    msg = getString(R.string.msg_subscribe_failed)
                }
                Log.d(TAG, msg)
//                Toast.makeText(baseContext, msg, Toast.LENGTH_SHORT).show()
            }
    }

    private fun adapterOnClick(msg: Message) {

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)

        /* Inserts message into viewModel. */
        if (requestCode == NEW_MESSAGE_ACTIVITY_INTENT_CODE && resultCode == Activity.RESULT_OK) {
            intent?.let { addMessageFromIntent(it) }
        }
    }

    private fun addMessageFromIntent(intent: Intent) {
        val title = intent.getStringExtra(MESSAGE_TITLE)
        val body = intent.getStringExtra(MESSAGE_BODY)
//                val received = data.getLongExtra(MESSAGE_RECEIVED)

        if (title == null || body == null) {
            return
        }

        messageListViewModel.insertMessage(title, body, Calendar.getInstance().time)
    }

    companion object {
        private const val TAG = "MainActivity"
    }
}