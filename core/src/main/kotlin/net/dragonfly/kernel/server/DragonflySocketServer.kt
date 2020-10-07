package net.dragonfly.kernel.server

import com.esotericsoftware.kryonet.*
import net.dragonfly.kernel.collector.ListenerCollector.registerListeners
import net.dragonfly.kernel.collector.PacketCollector.registerPackets
import net.dragonfly.kernel.logger.SocketLogger
import net.dragonfly.kernel.server.listener.ServerListenerSupplier
import net.dragonfly.kernel.server.session.SessionManager.sessionOrNull
import net.dragonfly.kernel.server.statistics.Statistics
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
            registerListeners(ServerListenerSupplier)
        }

        fixedRateTimer("Keep Alive Inspector", true, 0, 1000 * 30) {
            server.connections.mapNotNull { it.sessionOrNull }
                .filter {
                    val lastKeepAlive = it.metadata["last_keep_alive"] as? Long ?: it.createdAt
                    System.currentTimeMillis() - lastKeepAlive > 1000 * 60 * 5
                }.forEach {
                    LogManager.getLogger().info("${it.connection} could not be kept alive...")
                    it.connection.close()
                }
        }

        fixedRateTimer("Online Time Tracker", true, 0, 1000 * 20) {
            server.connections.mapNotNull { it.sessionOrNull }
                .filter {
                    val lastKeepActive = it.metadata["last_keep_active"] as? Long ?: it.createdAt
                    val inactive = it.metadata["inactive"] as? Boolean ?: true
                    !inactive && System.currentTimeMillis() - lastKeepActive > 1000 * 40
                }.forEach {
                    Statistics.updateOnlineTime(it)
                    it.metadata["first_keep_active"] = null
                    it.metadata["inactive"] = true
                    LogManager.getLogger().info("${it.account.username} is now afk.")
                }
        }
    }
}
