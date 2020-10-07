package net.dragonfly.kernel.collector

import com.esotericsoftware.kryonet.EndPoint
import net.dragonfly.kernel.collector.ListenerCollector.packetListeners
import net.dragonfly.kernel.packets.Packet
import net.dragonfly.kernel.packets.client.*
import net.dragonfly.kernel.packets.server.OnlineAccountsPacket
import net.dragonfly.kernel.packets.server.StartSessionResponsePacket
import org.apache.logging.log4j.LogManager
import org.reflections.Reflections
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder
import kotlin.properties.Delegates

/**
 * Responsible for collecting all packets.
 */
object PacketCollector {

    /**
     * All collected packets. Values are stored when invoking the [collectPackets] functions.
     */
    var packets by Delegates.notNull<Collection<Class<out Packet>>>()
        private set

    /**
     * Collects the packets and outputs the result to the [packetListeners] property.
     */
    private fun collectPackets() {
        packets = listOf(
            KeepActivePacket::class.java,
            KeepAlivePacket::class.java,
            StartSessionRequestPacket::class.java,
            StartSessionResponsePacket::class.java,
            UpdateMinecraftAccountPacket::class.java,
            OnlineAccountsPacket::class.java
        ).sortedBy { it.name }

        LogManager.getLogger().info("Collected ${packets.size} packets: " + packets.joinToString { it.simpleName })
    }

    /**
     * Registers the collected packets on the [EndPoint] instance.
     */
    fun EndPoint.registerPackets() {
        collectPackets()
        packets.forEach { kryo.register(it) }
        kryo.register(Array<String>::class.java)
    }
}
