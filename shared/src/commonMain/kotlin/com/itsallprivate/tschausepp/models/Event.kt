package com.itsallprivate.tschausepp.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
sealed interface Event

@Serializable
@SerialName("message")
data class Message(
    val sender: String,
    val content: String,
) : Event
