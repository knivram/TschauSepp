package com.itsallprivate.tschausepp.plugins

import com.itsallprivate.tschausepp.domain.game.Game
import com.itsallprivate.tschausepp.json
import com.itsallprivate.tschausepp.models.Event
import com.itsallprivate.tschausepp.models.Message
import io.ktor.serialization.deserialize
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.routing.routing
import io.ktor.server.websocket.WebSockets
import io.ktor.server.websocket.converter
import io.ktor.server.websocket.pingPeriod
import io.ktor.server.websocket.timeout
import io.ktor.server.websocket.webSocket
import io.ktor.websocket.Frame
import kotlinx.coroutines.channels.consumeEach
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
        val game = Game()
        webSocket("/ws") {
            val converter = converter ?: throw IllegalStateException("No converter found")
            val username = call.parameters["username"] ?: "Anonymous"
            game.addUser(username, this)

            runCatching {
                incoming.consumeEach { frame ->
                    if (frame is Frame.Text) {
                        val receivedEvent = converter.deserialize<Event>(frame)
                        when (receivedEvent) {
                            is Message -> {
                                println("Server: Received message: ${receivedEvent.content}")
                            }
                        }
                    }
                }
            }.onFailure { exception ->
                println("WebSocket exception: ${exception.localizedMessage}")
            }
        }
    }
}
