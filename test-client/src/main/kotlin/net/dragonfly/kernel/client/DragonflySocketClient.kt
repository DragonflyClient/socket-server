package net.dragonfly.kernel.client

import com.esotericsoftware.kryonet.*
import net.dragonfly.kernel.client.listener.ClientListenerSupplier
import net.dragonfly.kernel.collector.ListenerCollector.registerListeners
import net.dragonfly.kernel.collector.PacketCollector.registerPackets
import net.dragonfly.kernel.logger.SocketLogger
import net.dragonfly.kernel.packets.client.*
import kotlin.concurrent.fixedRateTimer
import kotlin.system.exitProcess

object DragonflySocketClient {
    lateinit var client: Client

    @JvmStatic
    fun main(args: Array<String>) {
        SocketLogger.setCustomLogger()

        client = Client().apply {
            start()
            connect(500, "127.0.0.1", 7331)
            registerPackets()
            registerListeners(ClientListenerSupplier)
            sendTCP(StartSessionRequestPacket(jwt = "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBdXRoZW50aWNhdGlvbiIsImlzcyI6ImluY2VwdGlvbm" +
                    "Nsb3VkLm5ldCIsImV4cCI6MTYwMzgzMDA1NCwidXVpZCI6ImNmYTlhMTBhLTNlMDgtNGU0NC1iYTNiLWI0ZDVhNjljYzRjNiJ9.Inx3Uagd8weaIHRZNr_1usfm" +
                    "QIK8Ws9jXvz_LOJ6imuONUtwYAV6KrkpZuon5zgkNNfZyQCXl5ED5TlDvXdMCw"))
            sendTCP(UpdateMinecraftAccountPacket("bc73becb-bc3b-487b-95a2-35ab00e11d0a"))
        }

        fixedRateTimer("Keep Alive Sender", false, 1000, 1000 * 60 * 2) {
            client.sendTCP(KeepAlivePacket())
        }

        fixedRateTimer("Keep Active Sender", false, 1000, 1000 * 30) {
            client.sendTCP(KeepActivePacket())
        }

        Thread.sleep(130_000)
        exitProcess(0)
    }
}
