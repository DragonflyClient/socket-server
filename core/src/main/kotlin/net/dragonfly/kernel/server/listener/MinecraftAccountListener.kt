package net.dragonfly.kernel.server.listener

import com.esotericsoftware.kryonet.Connection
import net.dragonfly.kernel.packets.client.UpdateMinecraftAccountPacket
import net.dragonfly.kernel.server.session.SessionManager.session
import net.dragonfly.kernel.server.users.UsersManagement
import net.dragonfly.kernel.server.utils.RateLimiter.consumeRateLimit

class MinecraftAccountListener {

    fun updateMinecraftAccount(connection: Connection, packet: UpdateMinecraftAccountPacket): Unit = synchronized(connection) {
        val previousAccount = connection.session.metadata["minecraft_account"] as? String
        val currentAccount = packet.currentMinecraftAccount
            ?.takeIf { connection.session.account.linkedMinecraftAccounts.contains(it) }

        if (previousAccount == currentAccount) return

        connection.consumeRateLimit("update_minecraft_account", 5_000) {
            connection.session.metadata["minecraft_account"] = currentAccount
            UsersManagement.switchMinecraftAccount(connection, previousAccount, currentAccount)
        }
    }
}