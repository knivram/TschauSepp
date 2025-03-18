package com.itsallprivate.tschausepp.domain.game

import com.itsallprivate.tschausepp.domain.model.User
import com.itsallprivate.tschausepp.models.Message
import com.itsallprivate.tschausepp.utlis.sendSerializedEvent
import io.ktor.server.websocket.WebSocketServerSession
import java.util.Collections.synchronizedList
import java.util.UUID

class Game {
    private val users = synchronizedList<User>(ArrayList())
    private var isStarted = false

    private fun start() {
        isStarted = true
    }

    suspend fun addUser(
        username: String,
        session: WebSocketServerSession,
    ) {
        users.forEach { user ->
            user.session.sendSerializedEvent(Message(sender = "Server", content = "$username connected"))
        }
        users.add(User(id = UUID.randomUUID(), name = username, session = session))
    }
}
