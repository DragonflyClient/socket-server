package net.dragonfly.kernel.server.listener

import com.esotericsoftware.kryonet.Connection
import com.esotericsoftware.kryonet.Listener
import net.dragonfly.kernel.collector.ServerListener
import net.dragonfly.kernel.server.session.SessionManager
import net.dragonfly.kernel.server.session.SessionManager.session
import net.dragonfly.kernel.server.statistics.Statistics
import org.apache.logging.log4j.LogManager

@ServerListener
class DisconnectListener : Listener() {

    override fun disconnected(connection: Connection) {
        Statistics.updateOnlineTime(connection.session)

        SessionManager.endSession(connection)
        LogManager.getLogger().info("Ended session of $connection")
    }
}
