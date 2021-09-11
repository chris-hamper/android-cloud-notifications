package com.chrishamper.statusnotifications.messageList

import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.RecyclerView
import com.chrishamper.statusnotifications.R
import com.chrishamper.statusnotifications.data.Message
import com.chrishamper.statusnotifications.messageDetail.MessageDetailActivity
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging

const val MESSAGE_ID = "message id"
const val MESSAGE_TITLE = "title"
const val MESSAGE_BODY = "body"
const val MESSAGE_SENT_TIME = "sent time"
const val NEW_MESSAGE_INTENT = "new message"
const val NEW_MESSAGE_ACTIVITY_INTENT_CODE = 1

class MessageListActivity : AppCompatActivity() {
    private val messageListViewModel by viewModels<MessageListViewModel> {
        MessageListViewModelFactory()
    }

    private val messageReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            intent?.let {
                addMessageFromIntent(intent)
            }
        }
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

    override fun onStart() {
        super.onStart()
        LocalBroadcastManager.getInstance(this).registerReceiver(messageReceiver,
            IntentFilter(NEW_MESSAGE_INTENT)
        )
    }

    override fun onStop() {
        super.onStop()
        LocalBroadcastManager.getInstance(this).unregisterReceiver(messageReceiver)
    }

    /* Opens MessageDetailActivity when RecyclerView item is clicked. */
    private fun adapterOnClick(message: Message) {
        val intent = Intent(this, MessageDetailActivity()::class.java)
        intent.putExtra(MESSAGE_ID, message.id)
        startActivity(intent)
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
//        super.onActivityResult(requestCode, resultCode, intent)
//
//        /* Inserts message into viewModel. */
//        if (requestCode == NEW_MESSAGE_ACTIVITY_INTENT_CODE && resultCode == Activity.RESULT_OK) {
//            intent?.let { addMessageFromIntent(it) }
//        }
//    }

    private fun addMessageFromIntent(intent: Intent) {
        val title = intent.getStringExtra(MESSAGE_TITLE)
        val body = intent.getStringExtra(MESSAGE_BODY)
        val sent = intent.getLongExtra(MESSAGE_SENT_TIME, Long.MIN_VALUE)
        val id = intent.getStringExtra(MESSAGE_ID)

        if (title == null || body == null || sent == Long.MIN_VALUE || id == null) {
            Log.e(TAG, "Intent didn't have all necessary extras")
            return
        }

        messageListViewModel.insertMessage(id, title, body, sent)
    }

    companion object {
        private const val TAG = "MessageListActivity"
    }
}