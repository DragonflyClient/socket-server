package net.dragonfly.socket.client

import com.esotericsoftware.kryonet.*
import net.dragonfly.socket.collector.ListenerCollector.registerListeners
import net.dragonfly.socket.collector.PacketCollector.registerPackets
import net.dragonfly.socket.logger.SocketLogger
import net.dragonfly.socket.packets.client.StartSessionRequestPacket

object DragonflySocketClient {
    lateinit var client: Client

    @JvmStatic
    fun main(args: Array<String>) {
        SocketLogger.setCustomLogger()

        client = Client().apply {
            start()
            connect(500, "127.0.0.1", 7331)
            registerPackets()
            registerListeners("net.dragonfly.socket.client.listener")
            sendTCP(StartSessionRequestPacket(jwt = "ey15n7ewt5g6735g56e423fr45.3h5j7T&4723g56g35r2.32j57g2gv5672f5f3"))
        }

        Thread.sleep(100_000)
    }
}
