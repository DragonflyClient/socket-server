package net.dragonfly.socket.server.listener

import com.esotericsoftware.kryonet.Connection
import net.dragonfly.socket.collector.PacketListener
import net.dragonfly.socket.packets.SessionAcceptedPackage
import net.dragonfly.socket.packets.SessionStartPackage
import org.apache.logging.log4j.LogManager

@PacketListener
class SessionListener {

    @PacketListener
    fun start(connection: Connection, packet: SessionStartPackage) {
        LogManager.getLogger().info("Starting session on $connection using JWT: " + packet.jwt)
        connection.sendTCP(SessionAcceptedPackage("it was good"))
    }
}
