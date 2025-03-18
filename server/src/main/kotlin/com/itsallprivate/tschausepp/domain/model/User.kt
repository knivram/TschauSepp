package com.itsallprivate.tschausepp.domain.model

import com.itsallprivate.tschausepp.models.Card
import io.ktor.server.websocket.WebSocketServerSession
import java.util.UUID

data class User(
    val id: UUID,
    val name: String,
    val session: WebSocketServerSession,
    val cards: List<Card> = emptyList(),
)
