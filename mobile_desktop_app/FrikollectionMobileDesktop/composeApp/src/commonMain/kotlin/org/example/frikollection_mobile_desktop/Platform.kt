package org.example.frikollection_mobile_desktop

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform