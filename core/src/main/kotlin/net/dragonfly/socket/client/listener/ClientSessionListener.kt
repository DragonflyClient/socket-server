package net.dragonfly.socket.client.listener

import com.esotericsoftware.kryonet.Connection
import net.dragonfly.socket.collector.PacketListener
import net.dragonfly.socket.packets.server.StartSessionResponsePacket
import org.apache.logging.log4j.LogManager

@PacketListener
class ClientSessionListener {

    @PacketListener
    fun response(connection: Connection, packet: StartSessionResponsePacket) {
        if (packet.success!!) {
            LogManager.getLogger().info("The session on $connection has been successfully opened")
        } else {
            LogManager.getLogger().info("The session on $connection could not be started")
        }
    }
}
