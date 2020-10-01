package net.dragonfly.socket.server.listener

import com.esotericsoftware.kryonet.Connection
import net.dragonfly.socket.collector.PacketListener
import net.dragonfly.socket.packets.client.KeepActivePacket
import net.dragonfly.socket.server.session.SessionManager.session
import org.apache.logging.log4j.LogManager

/**
 * Listens for [KeepActivePacket]s and sets the `last_keep_active` property of the session's metadata
 * to the time when the packet was received.
 */
@PacketListener
class KeepActiveListener {

    @PacketListener
    fun keepActive(connection: Connection, packet: KeepActivePacket) = synchronized(connection) {
        val metadata = connection.session.metadata
        val inactive = metadata["inactive"] as? Boolean

        if (inactive != false) {
            LogManager.getLogger().info("${connection.session.account.username} is no longer afk.")
            metadata["first_keep_active"] = System.currentTimeMillis()
            metadata["inactive"] = false
        }

        metadata["last_keep_active"] = System.currentTimeMillis()
    }
}
