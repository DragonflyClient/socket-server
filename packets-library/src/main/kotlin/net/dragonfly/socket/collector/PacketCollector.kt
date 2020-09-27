package net.dragonfly.socket.collector

import com.esotericsoftware.kryo.Kryo
import com.esotericsoftware.kryonet.EndPoint
import net.dragonfly.socket.collector.ListenerCollector.listeners
import net.dragonfly.socket.packets.Packet
import org.apache.logging.log4j.LogManager
import org.reflections.Reflections
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder
import kotlin.properties.Delegates

/**
 * Responsible for collecting all packets that extend the [Packet] interface in the
 * `net.dragonfly.socket.packets` package.
 */
object PacketCollector {
    /**
     * Reflections instance used for collecting these classes.
     */
    private val reflections = Reflections(ConfigurationBuilder().setUrls(
        ClasspathHelper.forPackage("net.dragonfly.socket.packets")
    ))

    /**
     * All collected packets. Values are stored when invoking the [collectPackets] functions.
     */
    var packets by Delegates.notNull<Set<Class<out Packet>>>()
        private set

    /**
     * Collects the packets using the [reflections] instance and outputs the result to
     * the [listeners] property.
     */
    fun collectPackets() {
        packets = reflections.getSubTypesOf(Packet::class.java)
        LogManager.getLogger().info("Collected ${packets.size} packets: " + packets.joinToString { it.simpleName })
    }

    /**
     * Registers the collected packets on the [EndPoint] instance.
     */
    fun EndPoint.registerPackets() {
        collectPackets()
        packets.forEach { kryo.register(it) }
    }
}
