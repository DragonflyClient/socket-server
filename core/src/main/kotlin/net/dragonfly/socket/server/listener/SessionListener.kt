package net.dragonfly.socket.server.listener

import com.esotericsoftware.kryonet.Connection
import net.dragonfly.socket.collector.PacketListener
import net.dragonfly.socket.packets.client.StartSessionRequestPacket
import net.dragonfly.socket.packets.server.StartSessionResponsePacket
import org.apache.logging.log4j.LogManager

@PacketListener
class SessionListener {

    @PacketListener
    fun request(connection: Connection, packet: StartSessionRequestPacket) {
        LogManager.getLogger().info("Starting session on $connection using JWT: " + packet.jwt)
        connection.sendTCP(StartSessionResponsePacket(true))
    }
}
