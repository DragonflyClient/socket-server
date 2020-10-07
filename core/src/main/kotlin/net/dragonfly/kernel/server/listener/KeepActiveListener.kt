package net.dragonfly.kernel.server.listener

import com.esotericsoftware.kryonet.Connection
import net.dragonfly.kernel.packets.client.KeepActivePacket
import net.dragonfly.kernel.server.session.SessionManager.session
import org.apache.logging.log4j.LogManager

/**
 * Listens for [KeepActivePacket]s and sets the `last_keep_active` property of the session's metadata
 * to the time when the packet was received.
 */
class KeepActiveListener {

    fun keepActive(connection: Connection, packet: KeepActivePacket) = synchronized(connection) {
        val metadata = connection.session.metadata
        val inactive = metadata["inactive"] as? Boolean

        if (inactive != false) {
            if (inactive == true) // if the user was afk before
                LogManager.getLogger().info("${connection.session.account.username} is no longer afk.")
            metadata["first_keep_active"] = System.currentTimeMillis()
            metadata["inactive"] = false
        }

        metadata["last_keep_active"] = System.currentTimeMillis()
    }
}
