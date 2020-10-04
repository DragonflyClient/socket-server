package net.dragonfly.kernel.collector

import com.esotericsoftware.kryonet.EndPoint
import net.dragonfly.kernel.collector.ListenerCollector.packetListeners
import net.dragonfly.kernel.packets.Packet
import org.apache.logging.log4j.LogManager
import org.reflections.Reflections
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder
import kotlin.properties.Delegates

/**
 * Responsible for collecting all packets that extend the [Packet] interface in the
 * `net.dragonfly.kernel.packets` package.
 */
object PacketCollector {
    /**
     * Reflections instance used for collecting these classes.
     */
    private val reflections = Reflections(ConfigurationBuilder().setUrls(
        ClasspathHelper.forPackage("net.dragonfly.kernel.packets")
    ))

    /**
     * All collected packets. Values are stored when invoking the [collectPackets] functions.
     */
    var packets by Delegates.notNull<List<Class<out Packet>>>()
        private set

    /**
     * Collects the packets using the [reflections] instance and outputs the result to
     * the [packetListeners] property.
     */
    fun collectPackets() {
        packets = reflections.getSubTypesOf(Packet::class.java).sortedBy { it.name }
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
