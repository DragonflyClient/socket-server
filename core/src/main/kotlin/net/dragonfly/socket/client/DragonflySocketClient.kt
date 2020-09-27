package net.dragonfly.socket.client

import com.esotericsoftware.kryonet.*
import net.dragonfly.socket.collector.ListenerCollector.registerListeners
import net.dragonfly.socket.collector.PacketCollector.registerPackets
import net.dragonfly.socket.logger.SocketLogger
import net.dragonfly.socket.packets.client.SessionInfoPacket
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
            sendTCP(StartSessionRequestPacket(jwt = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBdXRoZW50aWNhdGlvbiIsImlzcyI6ImluY2VwdGlvbm" +
                    "Nsb3VkLm5ldCIsImV4cCI6MTYwMzgzMDA1NCwidXVpZCI6ImNmYTlhMTBhLTNlMDgtNGU0NC1iYTNiLWI0ZDVhNjljYzRjNiJ9.Inx3Uagd8weaIHRZNr_1usfm" +
                    "QIK8Ws9jXvz_LOJ6imuONUtwYAV6KrkpZuon5zgkNNfZyQCXl5ED5TlDvXdMCw"))
        }

        Thread.sleep(5_000)

        client.sendTCP(SessionInfoPacket())

        Thread.sleep(100_000)
    }
}
