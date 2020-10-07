package net.dragonfly.kernel.server.listener

import com.esotericsoftware.kryonet.Connection
import net.dragonfly.kernel.packets.client.KeepAlivePacket
import net.dragonfly.kernel.server.session.SessionManager.session

/**
 * Listens for [KeepAlivePacket]s and sets the `last_keep_alive` property of the session's metadata
 * to the time when the packet was received.
 */
class KeepAliveListener {

    fun keepAlive(connection: Connection, packet: KeepAlivePacket) = synchronized(connection) {
        val metadata = connection.session.metadata
        metadata["last_keep_alive"] = System.currentTimeMillis()
    }
}
