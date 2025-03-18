package com.itsallprivate.tschausepp.utlis

import com.itsallprivate.tschausepp.models.Event
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.sendSerialized

suspend fun DefaultClientWebSocketSession.sendSerializedEvent(event: Event) {
    sendSerialized(event)
}
