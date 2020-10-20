package net.dragonfly.kernel.server.listener

import com.esotericsoftware.kryonet.Connection
import com.mongodb.client.model.Filters
import com.mongodb.client.model.Updates
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import net.dragonfly.kernel.packets.client.StartSessionRequestPacket
import net.dragonfly.kernel.packets.server.StartSessionResponsePacket
import net.dragonfly.kernel.server.Database
import net.dragonfly.kernel.server.session.SessionManager
import org.apache.logging.log4j.LogManager

/**
 * Packet listener that handles all session-specific packets like starting or ending
 * a session.
 */
class SessionListener {

    /**
     * Starts a new session on an incoming [StartSessionRequestPacket].
     */
    fun startSession(connection: Connection, packet: StartSessionRequestPacket) = synchronized(connection) {
        val jwt = packet.jwt!!

        LogManager.getLogger().info("Starting session on $connection...")
        val session = SessionManager.buildSession(connection, jwt)

        if (session != null) {
            LogManager.getLogger().info("Session started: $session")
            GlobalScope.launch {
                Database.accountsCollection.updateOne(Filters.eq("uuid", session.account.uuid), Updates.set("metadata.played_in_alpha", true))
            }
            connection.sendTCP(StartSessionResponsePacket(true))
        } else {
            LogManager.getLogger().info("Failed to start session on $connection")
            connection.sendTCP(StartSessionResponsePacket(false))
        }
    }
}
