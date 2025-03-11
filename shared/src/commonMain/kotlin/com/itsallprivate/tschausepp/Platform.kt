package com.itsallprivate.tschausepp

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform