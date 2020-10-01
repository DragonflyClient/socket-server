package net.dragonfly.socket.client

import com.esotericsoftware.kryonet.*
import net.dragonfly.socket.collector.ListenerCollector.registerListeners
import net.dragonfly.socket.collector.PacketCollector.registerPackets
import net.dragonfly.socket.logger.SocketLogger
import net.dragonfly.socket.packets.client.*
import kotlin.concurrent.fixedRateTimer

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

        fixedRateTimer("Keep Alive Sender", daemon = false, 1000, 1000 * 60 * 2) {
            client.sendTCP(KeepAlivePacket())
        }

        val timer = fixedRateTimer("Keep Active Sender", daemon = false, 1000, 1000 * 30) {
            client.sendTCP(KeepActivePacket())
        }

        Thread.sleep(100_000)
        timer.cancel()

        Thread.sleep(100_000)
        fixedRateTimer("Keep Active Sender", daemon = false, 1000, 1000 * 30) {
            client.sendTCP(KeepActivePacket())
        }
    }
}
