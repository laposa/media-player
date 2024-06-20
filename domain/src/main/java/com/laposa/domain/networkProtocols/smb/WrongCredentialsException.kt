package com.laposa.domain.networkProtocols.smb

class WrongCredentialsException : Exception() {
    override val message: String
        get() = "Wrong credentials"
}