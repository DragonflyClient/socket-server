package net.dragonfly.socket.logger

import com.esotericsoftware.minlog.Log
import org.apache.logging.log4j.Level
import org.apache.logging.log4j.LogManager

/**
 * Responsible for changing the default logging behavior of kryonet.
 */
object SocketLogger {
    /**
     * Changes the kryonet logger to a new instance of the [CustomSocketLogger] class
     * that redirects log messages to log4j.
     */
    fun setCustomLogger() {
        Log.setLogger(CustomSocketLogger())
    }
}

/**
 * A customized logger implementation to override the default logging behavior of kryonet.
 */
private class CustomSocketLogger : Log.Logger() {
    override fun log(level: Int, category: String?, message: String?, ex: Throwable?) {
        val logger = category?.let { LogManager.getLogger(category) } ?: LogManager.getLogger()
        val apacheLevel = when (level) {
            Log.LEVEL_ERROR -> Level.ERROR
            Log.LEVEL_WARN -> Level.WARN
            Log.LEVEL_INFO -> Level.INFO
            Log.LEVEL_DEBUG -> Level.DEBUG
            Log.LEVEL_TRACE -> Level.TRACE
            else -> Level.TRACE
        }

        if (ex != null) {
            logger.log(apacheLevel, message, ex)
        } else {
            logger.log(apacheLevel, message)
        }
    }

    override fun print(message: String?) {
        throw UnsupportedOperationException("Printing is not supported by the custom logger!")
    }
}
