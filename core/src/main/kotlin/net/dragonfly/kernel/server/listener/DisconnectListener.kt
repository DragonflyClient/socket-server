package net.dragonfly.kernel.server.listener

import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import net.dragonfly.kernel.server.session.SessionManager
import net.dragonfly.kernel.server.session.SessionManager.session
import net.dragonfly.kernel.server.statistics.Statistics
import org.apache.logging.log4j.LogManager

class DisconnectListener : Listener() {

    override fun disconnected(connection: Connection) = synchronized(connection) {
        try {
            connection.session.metadata["last_keep_active"] = System.currentTimeMillis()
            Statistics.updateOnlineTime(connection.session)
            SessionManager.endSession(connection)

            LogManager.getLogger().info("Ended session of $connection")
        } catch (e: Throwable) {
            LogManager.getLogger().warn("Could not end session of $connection.")
        }
    }
}
