package net.dragonfly.socket.server.listener

import com.esotericsoftware.kryonet.Connection
import net.dragonfly.socket.collector.PacketListener
import net.dragonfly.socket.packets.client.StartSessionRequestPacket
import net.dragonfly.socket.packets.server.StartSessionResponsePacket
import net.dragonfly.socket.server.session.SessionManager
import org.apache.logging.log4j.LogManager

/**
 * Packet listener that handles all session-specific packets like starting or ending
 * a session.
 */
@PacketListener
class SessionListener {

    /**
     * Starts a new session on an incoming [StartSessionRequestPacket].
     */
    @PacketListener
    fun startSession(connection: Connection, packet: StartSessionRequestPacket) = synchronized(connection) {
        val jwt = packet.jwt!!

        LogManager.getLogger().info("Starting session on $connection...")
        val session = SessionManager.buildSession(connection, jwt)

        if (session != null) {
            LogManager.getLogger().info("Session started: $session")
            connection.sendTCP(StartSessionResponsePacket(true))
        } else {
            LogManager.getLogger().info("Failed to start session on $connection")
            connection.sendTCP(StartSessionResponsePacket(false))
        }
    }
}
