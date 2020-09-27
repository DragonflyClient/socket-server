package net.dragonfly.socket.client

import com.esotericsoftware.kryonet.Client
import net.dragonfly.socket.collector.ListenerCollector.registerListeners
import net.dragonfly.socket.collector.PacketCollector
import net.dragonfly.socket.collector.PacketCollector.registerPackets
import net.dragonfly.socket.logger.SocketLogger
import net.dragonfly.socket.packets.SessionStartPackage

object DragonflySocketClient {
    lateinit var client: Client

    @JvmStatic
    fun main(args: Array<String>) {
        SocketLogger.setCustomLogger()

        client = Client()
        client.start()
        client.connect(500, "127.0.0.1", 7331)
        client.registerPackets()
        client.registerListeners("net.dragonfly.socket.client.listener")
        client.sendTCP(SessionStartPackage(jwt = "ey15n7ewt5g6735g56e423fr45.3h5j7T&4723g56g35r2.32j57g2gv5672f5f3"))
    }
}
