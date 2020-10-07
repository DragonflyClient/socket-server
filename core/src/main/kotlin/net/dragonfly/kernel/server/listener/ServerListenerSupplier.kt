package net.dragonfly.kernel.server.listener

import net.dragonfly.kernel.collector.ListenerSupplier

object ServerListenerSupplier : ListenerSupplier {

    override fun getServerListeners() = listOf(
        DisconnectListener::class
    )

    override fun getPacketListeners() = listOf(
        KeepActiveListener::class, KeepAliveListener::class, MinecraftAccountListener::class, SessionListener::class
    )
}