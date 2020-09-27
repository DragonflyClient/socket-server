package net.dragonfly.socket.server.listener

import com.esotericsoftware.kryonet.Connection
import net.dragonfly.socket.collector.PacketListener
import net.dragonfly.socket.packets.client.SessionInfoPacket
import net.dragonfly.socket.packets.client.StartSessionRequestPacket
import net.dragonfly.socket.packets.server.StartSessionResponsePacket
import net.dragonfly.socket.server.session.SessionManager
import net.dragonfly.socket.server.session.SessionManager.session
import org.apache.logging.log4j.LogManager

@PacketListener
class SessionListener {

    @PacketListener
    fun request(connection: Connection, packet: StartSessionRequestPacket) {
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

    @PacketListener
    fun getSessionInfo(connection: Connection, packet: SessionInfoPacket) {
        println(connection.session)
    }
}
