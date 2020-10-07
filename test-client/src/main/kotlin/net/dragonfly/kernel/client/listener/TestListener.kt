package net.dragonfly.kernel.client.listener

import com.esotericsoftware.kryonet.Connection
import net.dragonfly.kernel.packets.server.OnlineAccountsPacket
import net.dragonfly.kernel.packets.server.StartSessionResponsePacket
import org.apache.logging.log4j.LogManager

class TestListener {

    fun response(connection: Connection, packet: StartSessionResponsePacket) {
        if (packet.success!!) {
            LogManager.getLogger().info("The session on $connection has been successfully opened")
        } else {
            LogManager.getLogger().info("The session on $connection could not be started")
        }
    }

    fun receiveOnlineAccounts(connection: Connection, packet: OnlineAccountsPacket) {
        LogManager.getLogger().info("Current online players are: ${packet.onlineAccounts?.joinToString()}");
    }
}
