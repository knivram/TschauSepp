package com.itsallprivate.tschausepp.data.deckofcards.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class DeckResponse(
    val success: Boolean,
    @SerialName("deck_id")
    val deckId: String,
    val shuffled: Boolean? = null,
    val remaining: Int,
)
