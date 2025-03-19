package com.itsallprivate.tschausepp.data.deckofcards

import com.itsallprivate.tschausepp.data.deckofcards.model.DeckResponse
import com.itsallprivate.tschausepp.data.deckofcards.model.DrawCardResponse
import com.itsallprivate.tschausepp.data.deckofcards.model.PileResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

const val BASE_URL = "https://deckofcards.com/api/deck"

object DeckOfCardsApi {
    private val client =
        HttpClient(CIO) {
            install(ContentNegotiation) {
                json(
                    Json { ignoreUnknownKeys = true },
                )
            }
        }

    suspend fun getNewShuffledDeck(): DeckResponse = client.get("$BASE_URL/new/shuffle/").body()

    suspend fun drawCards(
        deckId: String,
        count: Int,
    ): DrawCardResponse =
        client
            .get("$BASE_URL/$deckId/draw/") {
                parameter("count", count)
            }.body()

    suspend fun shuffleDeck(deckId: String): DeckResponse = client.get("$BASE_URL/$deckId/shuffle/").body()

    suspend fun addToPile(
        deckId: String,
        pileName: String,
        cardIds: List<String>,
    ): PileResponse =
        client
            .get("$BASE_URL/$deckId/pile/$pileName/add/") {
                parameter("cards", cardIds.joinToString(","))
            }.body()

    suspend fun listPile(
        deckId: String,
        pileName: String,
    ): PileResponse = client.get("$BASE_URL/$deckId/pile/$pileName/list/").body()

    suspend fun drawFromPile(
        deckId: String,
        pileName: String,
        cardIds: List<String>,
    ): PileResponse =
        client
            .get("$BASE_URL/$deckId/pile/$pileName/draw/") {
                parameter("cards", cardIds.joinToString(","))
            }.body()
}
