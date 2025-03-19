package com.itsallprivate.tschausepp.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Card(
    val code: String,
    val image: String,
    val value: CardValue,
    val suit: Suit,
)

@Serializable
enum class Suit {
    HEARTS,
    DIAMONDS,
    CLUBS,
    SPADES,
}

@Serializable
enum class CardValue {
    ACE,

    @SerialName("2")
    TWO,

    @SerialName("3")
    THREE,

    @SerialName("4")
    FOUR,

    @SerialName("5")
    FIVE,

    @SerialName("6")
    SIX,

    @SerialName("7")
    SEVEN,

    @SerialName("8")
    EIGHT,

    @SerialName("9")
    NINE,

    @SerialName("10")
    TEN,
    JACK,
    QUEEN,
    KING,
}
