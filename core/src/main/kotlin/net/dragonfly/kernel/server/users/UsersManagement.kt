package net.dragonfly.kernel.server.users

import com.esotericsoftware.kryonet.Connection
import net.dragonfly.kernel.packets.server.OnlineAccountsPacket
import net.dragonfly.kernel.server.DragonflySocketServer
import net.dragonfly.kernel.server.session.Session
import org.apache.logging.log4j.LogManager

object UsersManagement {

    val onlineAccounts = mutableListOf<String>()

    fun switchMinecraftAccount(connection: Connection, previous: String?, current: String?) {
        LogManager.getLogger().info("$connection has switched their Minecraft account from $previous to $current")

        previous?.let { onlineAccounts.remove(it) }
        current?.let { onlineAccounts.add(it) }

        if (previous != null || current != null) {
            sendOnlineAccountsPacket()
        }
    }

    fun endSession(session: Session) {
        val account = session.metadata["minecraft_account"] as? String
        if (account != null) {
            onlineAccounts.remove(account)
            sendOnlineAccountsPacket()
        }
    }

    private fun sendOnlineAccountsPacket() {
        DragonflySocketServer.server.sendToAllTCP(OnlineAccountsPacket(onlineAccounts.toTypedArray()))
    }
}