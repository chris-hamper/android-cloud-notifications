package com.chrishamper.statusnotifications.data

import java.util.*

data class Message(
    val title: String,
    val body: String,
    val received: Date,
)
