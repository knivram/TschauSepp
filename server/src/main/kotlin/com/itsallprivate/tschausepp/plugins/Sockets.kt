package com.itsallprivate.tschausepp.plugins

import com.itsallprivate.tschausepp.json
import com.itsallprivate.tschausepp.models.Event
import com.itsallprivate.tschausepp.models.Message
import com.itsallprivate.tschausepp.utlis.sendSerializedEvent
import io.ktor.serialization.deserialize
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSocketServerSession
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.converter
import io.ktor.server.websocket.pingPeriod
import io.ktor.server.websocket.timeout
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.Frame
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import java.util.Collections.synchronizedList
import kotlin.time.Duration.Companion.seconds

fun Application.configureSockets() {
    install(WebSockets) {
        pingPeriod = 15.seconds
        timeout = 15.seconds
        maxFrameSize = Long.MAX_VALUE
        masking = false
        contentConverter = KotlinxWebsocketSerializationConverter(json)
    }
    routing {
        val sessions = synchronizedList<WebSocketServerSession>(ArrayList())
        webSocket("/ws") {
            sessions.add(this)
            val converter = converter ?: throw IllegalStateException("No converter found")
            val messageResponseFlow = MutableSharedFlow<Message>()
            val sharedFlow = messageResponseFlow.asSharedFlow()

            sendSerializedEvent(Message(sender = "Server", content = "You are connected to WebSocket!"))
            println("Server: You are connected to WebSocket!")

            val job =
                launch {
                    sharedFlow.collect { message ->
                        for (session in sessions) {
                            session.sendSerializedEvent(message)
                        }
                    }
                }

            runCatching {
                incoming.consumeEach { frame ->
                    if (frame is Frame.Text) {
                        val receivedMessage = converter.deserialize<Event>(frame)
                        when (receivedMessage) {
                            is Message -> {
                                println("Server: Received message: $receivedMessage")
                            }
                        }
                        messageResponseFlow.emit(receivedMessage)
                    }
                }
            }.onFailure { exception ->
                println("WebSocket exception: ${exception.localizedMessage}")
            }.also {
                job.cancel()
            }
        }
    }
}
