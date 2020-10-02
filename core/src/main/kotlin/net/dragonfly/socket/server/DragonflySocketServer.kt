package net.dragonfly.socket.server

import com.esotericsoftware.kryonet.*
import net.dragonfly.socket.collector.ListenerCollector.registerListeners
import net.dragonfly.socket.collector.PacketCollector.registerPackets
import net.dragonfly.socket.logger.SocketLogger
import net.dragonfly.socket.server.session.SessionManager.sessionOrNull
import net.dragonfly.socket.server.statistics.Statistics
import org.apache.logging.log4j.LogManager
import kotlin.concurrent.fixedRateTimer

object DragonflySocketServer {

    /**
     * Instance of the TCP server that receives the connections.
     */
    lateinit var server: Server

    @JvmStatic
    fun main(args: Array<String>) {
        SocketLogger.setCustomLogger()

        server = Server().apply {
            start()
            bind(7331)
            registerPackets()
            registerListeners("net.dragonfly.socket.server")
        }

        fixedRateTimer("Keep Alive Inspector", daemon = true, 0, 1000 * 30) {
            server.connections.mapNotNull { it.sessionOrNull }
                .filter {
                    val lastKeepAlive = it.metadata["last_keep_alive"] as? Long ?: it.createdAt
                    System.currentTimeMillis() - lastKeepAlive > 1000 * 60 * 5
                }.forEach {
                    LogManager.getLogger().info("${it.connection} could not be kept alive...")
                    it.connection.close()
                }
        }

        fixedRateTimer("Online Time Tracker", daemon = true, 0, 1000 * 20) {
            server.connections.mapNotNull { it.sessionOrNull }
                .filter {
                    val lastKeepActive = it.metadata["last_keep_active"] as? Long ?: it.createdAt
                    val inactive = it.metadata["inactive"] as? Boolean ?: false
                    !inactive && System.currentTimeMillis() - lastKeepActive > 1000 * 60 * 2
                }.forEach {
                    it.metadata["first_active_time"] = null
                    it.metadata["inactive"] = true
                    LogManager.getLogger().info("${it.account.username} is now afk.")
                    Statistics.updateOnlineTime(it)
                }
        }
    }
}
