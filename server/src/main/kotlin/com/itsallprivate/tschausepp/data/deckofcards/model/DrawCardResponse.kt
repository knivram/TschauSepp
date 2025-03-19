package com.itsallprivate.tschausepp.data.deckofcards.model

import com.itsallprivate.tschausepp.models.Card
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DrawCardResponse(
    val success: Boolean,
    @SerialName("deck_id")
    val deckId: String,
    val cards: List<Card>,
    val remaining: Int,
)
