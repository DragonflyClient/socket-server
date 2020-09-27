package net.dragonfly.socket.client.listener

import com.esotericsoftware.kryonet.Connection
import net.dragonfly.socket.collector.PacketListener
import net.dragonfly.socket.packets.SessionAcceptedPackage
import org.apache.logging.log4j.LogManager

@PacketListener
class ClientSessionListener {

    @PacketListener
    fun accepted(connection: Connection, packet: SessionAcceptedPackage) {
        LogManager.getLogger().info("Session on $connection has been accepted because ${packet.reason}")
    }
}
