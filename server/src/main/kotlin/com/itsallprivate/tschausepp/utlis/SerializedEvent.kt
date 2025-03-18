package com.itsallprivate.tschausepp.utlis

import com.itsallprivate.tschausepp.models.Event
import io.ktor.server.websocket.WebSocketServerSession
import io.ktor.server.websocket.sendSerialized

suspend fun WebSocketServerSession.sendSerializedEvent(event: Event) {
    sendSerialized(event)
}
