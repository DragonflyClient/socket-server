package net.dragonfly.socket.server

import com.esotericsoftware.kryonet.Server
import net.dragonfly.socket.collector.ListenerCollector.registerListeners
import net.dragonfly.socket.collector.PacketCollector.registerPackets
import net.dragonfly.socket.logger.SocketLogger

object DragonflySocketServer {
    lateinit var server: Server

    @JvmStatic
    fun main(args: Array<String>) {
        SocketLogger.setCustomLogger()

        server = Server().apply {
            start()
            bind(7331)
            registerPackets()
            registerListeners("net.dragonfly.socket.server.listener")
        }
    }
}
