package ie.laposa.domain.rememberLogin

import ie.laposa.domain.savedState.SavedStateService

class RememberLoginService(
    private val savedStateService: SavedStateService,
) {
    fun rememberLogin(mediaSourceIdentifier: String, userName: String, password: String) {
        savedStateService.setRememberLogin(mediaSourceIdentifier, RememberLogin(userName, password))
    }

    fun getRememberedLogin(mediaSourceIdentifier: String): RememberLogin? {
        return savedStateService.getRememberLogin(mediaSourceIdentifier)
    }

    fun clearRememberedLogin(mediaSourceIdentifier: String) {
        savedStateService.clearRememberLogin(mediaSourceIdentifier)
    }
}