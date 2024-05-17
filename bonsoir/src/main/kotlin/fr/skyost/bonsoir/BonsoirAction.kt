package fr.skyost.bonsoir

import android.net.nsd.NsdManager
import android.os.Handler
import android.os.Looper
import android.util.Log

/**
 * Allows to execute a network action.
 *
 * @param id The listener identifier.
 * @param action The action.
 * @param printLogs Whether to print debug logs.
 * @param onDispose Triggered when this instance is being disposed.
 * @param nsdManager The NSD manager instance.
 * @param messenger The Flutter binary messenger.
 */
abstract class BonsoirAction(
    protected val id: Int,
    private val action: String,
    protected val logMessages: Map<String, String>,
    private val printLogs: Boolean,
    private val onDispose: Runnable,
    protected val nsdManager: NsdManager,
) {
    companion object {
        /**
         * The log tag.
         */
        private const val tag: String = "bonsoir"
    }

    /**
     * Whether the discovery is active.
     */
    var isActive = false
        private set

    /**
     * Triggered when a success occurs.
     *
     * @param eventId The event id.
     * @param service The service involved.
     * @param message The message.
     * @param parameters The message parameters.
     */
    fun onSuccess(eventId: String, service: BonsoirService? = null, message: String? = null, parameters: List<Any> = emptyList()) {
        val logMessage = message ?: logMessages[eventId]!!
        val logParameters = ArrayList(parameters)
        if (service != null && !parameters.contains(service)) {
            logParameters.add(0, service)
        }
        log(logMessage, logParameters)
        Handler(Looper.getMainLooper())
    }

    /**
     * Triggered when an error occurs.
     *
     * @param message The message.
     * @param parameters The message parameters.
     * @param details The error details.
     */
    fun onError(message: String? = null, parameters: List<Any>, details: Any? = null) {
        val logMessage = format(message ?: logMessages["${action}Error"]!!, parameters)
        log(logMessage)
    }

    /**
     * Makes this action active.
     */
    fun makeActive() {
        isActive = true
    }

    /**
     * Makes this action unactive.
     */
    fun makeUnactive() {
        isActive = false
    }

    /**
     * Stops this action.
     */
    abstract fun stop()

    /**
     * Disposes the current class instance.
     *
     * @param notify Whether to notify listeners.
     */
    open fun dispose(notify: Boolean = isActive) {
        if (isActive) {
            isActive = false
            stop()
        }
        if (notify) {
            onDispose.run()
        }
    }

    /**
     * Logs the given message to the console, if enabled.
     *
     * @param message The message.
     */
    fun log(message: String, parameters: List<Any> = emptyList()) {
        if (printLogs) {
            Log.d(tag, "[$action] [$id] ${format(message, parameters)}")
        }
    }

    /**
     * Formats a given message with the given parameters.
     *
     * @param message The message.
     * @param parameters The parameters.
     */
    private fun format(message: String, parameters: List<Any>): String {
        var result: String = message
        for (parameter in parameters) {
            result = result.replaceFirst("%s", parameter.toString())
        }
        return result;
    }
}