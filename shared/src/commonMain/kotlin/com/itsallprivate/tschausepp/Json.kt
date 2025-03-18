package com.itsallprivate.tschausepp

import kotlinx.serialization.json.Json

val json =
    Json {
        classDiscriminator = "type"
        isLenient = true
    }
