package com.laposa.domain.networkProtocols

val mediaFileExtensionsList = listOf(
    ".mp4", ".mkv", ".avi", ".mov", ".flv", ".wmv", ".webm", ".m4v", ".mp3"
)

class AuthFailException : Exception("Authentication failed")
class ConnectionFailedException : Exception("Connection failed")