package com.itsallprivate.tschausepp

import io.ktor.client.engine.HttpClientEngineConfig
import io.ktor.client.engine.HttpClientEngineFactory

expect fun getKtorEngine(): HttpClientEngineFactory<HttpClientEngineConfig>