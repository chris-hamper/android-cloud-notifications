package com.chrishamper.statusnotifications

import android.app.Application
import com.chrishamper.statusnotifications.data.MessageRepository
import com.chrishamper.statusnotifications.data.MessageRoomDatabase

class MyApplication: Application() {
    private val database by lazy { MessageRoomDatabase.getDatabase(this) }
    val repo by lazy { MessageRepository(database.messageDao()) }
}