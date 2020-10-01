package net.dragonfly.socket.server.listener

import com.esotericsoftware.kryonet.Connection
import net.dragonfly.socket.collector.PacketListener
import net.dragonfly.socket.packets.client.KeepAlivePacket
import net.dragonfly.socket.server.session.SessionManager.session
import org.apache.logging.log4j.LogManager

/**
 * Listens for [KeepAlivePacket]s and sets the `last_keep_alive` property of the session's metadata
 * to the time when the packet was received.
 */
@PacketListener
class KeepAliveListener {

    @PacketListener
    fun keepAlive(connection: Connection, packet: KeepAlivePacket) = synchronized(connection) {
        val metadata = connection.session.metadata
        metadata["last_keep_alive"] = System.currentTimeMillis()
        LogManager.getLogger().info("$connection has been kept alive ;)")
    }
}
