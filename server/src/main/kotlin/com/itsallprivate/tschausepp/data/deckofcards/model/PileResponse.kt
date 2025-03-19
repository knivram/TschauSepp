package com.itsallprivate.tschausepp.data.deckofcards.model

import com.itsallprivate.tschausepp.models.Card
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PileResponse(
    val success: Boolean,
    @SerialName("deck_id")
    val deckId: String,
    val remaining: Int,
    val piles: Map<String, Pile>? = null,
    val cards: List<Card>? = null,
)

@Serializable
data class Pile(
    val remaining: Int,
    val cards: List<Card>? = null,
)
