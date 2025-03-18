package com.itsallprivate.tschausepp

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.itsallprivate.tschausepp.models.Event
import com.itsallprivate.tschausepp.models.Message
import com.itsallprivate.tschausepp.utlis.sendSerializedEvent
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.receiveDeserialized
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.http.HttpMethod
import io.ktor.serialization.kotlinx.KotlinxWebsocketSerializationConverter
import kotlinx.coroutines.launch
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    val client =
        HttpClient(getKtorEngine()) {
            install(WebSockets) {
                contentConverter = KotlinxWebsocketSerializationConverter(json)
                pingIntervalMillis = 20_000
            }
        }
    var coroutineScope = rememberCoroutineScope()
    var messages by remember { mutableStateOf<List<Message>>(listOf(Message("Marvin", "Test"))) }
    var message by remember { mutableStateOf<String>("") }
    var activeSocket: DefaultClientWebSocketSession? = null

    LaunchedEffect(Unit) {
        client.webSocket(method = HttpMethod.Get, host = "127.0.0.1", port = SERVER_PORT, path = "/ws") {
            activeSocket = this
            val initialMessage = Message(sender = "Client", content = "Hello from Compose!")
            sendSerializedEvent(initialMessage)
            while (true) {
                val receivedEvent = receiveDeserialized<Event>()
                when (receivedEvent) {
                    is Message -> {
                        messages = messages + receivedEvent
                    }
                }
            }
        }
    }

    MaterialTheme {
        Scaffold(Modifier.fillMaxWidth()) {
            LazyColumn(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                item {
                    TextField(message, onValueChange = { message = it }, label = { Text("Message") })
                    Button(
                        onClick = {
                            if (message.isEmpty()) return@Button
                            coroutineScope.launch {
                                val userMessage = Message(sender = "Client", content = message)
                                activeSocket?.sendSerializedEvent(userMessage)
                                message = ""
                            }
                        },
                    ) {
                        Text("Send")
                    }
                }
                items(messages) { message ->
                    Row {
                        Text(message.sender, style = MaterialTheme.typography.body1)
                        Text(message.content, style = MaterialTheme.typography.body2)
                    }
                }
            }
        }
    }
}
