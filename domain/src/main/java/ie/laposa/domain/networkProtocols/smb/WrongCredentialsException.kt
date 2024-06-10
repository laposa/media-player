package ie.laposa.domain.networkProtocols.smb

class WrongCredentialsException : Exception() {
    override val message: String
        get() = "Wrong credentials"
}