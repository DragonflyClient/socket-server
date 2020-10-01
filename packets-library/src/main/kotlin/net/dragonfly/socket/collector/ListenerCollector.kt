package net.dragonfly.socket.collector

import com.esotericsoftware.kryonet.*
import com.google.common.util.concurrent.ThreadFactoryBuilder
import org.apache.logging.log4j.LogManager
import org.reflections.Reflections
import org.reflections.util.ClasspathHelper
import org.reflections.util.ConfigurationBuilder
import java.util.concurrent.Executors
import kotlin.properties.Delegates
import kotlin.reflect.KClass
import kotlin.reflect.KParameter
import kotlin.reflect.full.*
import kotlin.reflect.jvm.jvmErasure

/**
 * Responsible for collecting all listeners annotated with [`@PacketListener`][PacketListener].
 */
object ListenerCollector {

    /**
     * All collected packet listeners. Values are stored when invoking the [collectListeners]
     * functions.
     */
    var packetListeners by Delegates.notNull<List<KClass<*>>>()
        private set

    /**
     * All collected server listeners. These ones are plain sub types of [Listener] and have
     * the advantage of more flexibility.
     */
    var serverListeners by Delegates.notNull<List<KClass<out Listener>>>()
        private set

    /**
     * The thread pool that is used to execute listeners.
     */
    private val threadPool = Executors.newCachedThreadPool(ThreadFactoryBuilder().setNameFormat("Listener-Executor-%d").build())

    /**
     * Collects the listeners in the given [package] recursively and outputs the result to
     * the [packetListeners] property.
     */
    fun collectListeners(`package`: String) {
        val reflections = Reflections(ConfigurationBuilder().setUrls(ClasspathHelper.forPackage(`package`)))
        packetListeners = reflections.getTypesAnnotatedWith(PacketListener::class.java).map { it.kotlin }
        serverListeners = reflections.getSubTypesOf(Listener::class.java).map { it.kotlin }.filter { it.hasAnnotation<ServerListener>() }
        LogManager.getLogger().info("Collected ${packetListeners.size} packet listeners: " + packetListeners.joinToString { it.simpleName!! })
        LogManager.getLogger().info("Collected ${serverListeners.size} server listeners: " + serverListeners.joinToString { it.simpleName!! })
    }

    /**
     * Registers the collected listeners from the given [package] on the [EndPoint] instance.
     */
    fun EndPoint.registerListeners(`package`: String) {
        collectListeners(`package`)
        packetListeners.forEach { addListener(it.createListener()) }
        serverListeners.forEach { addListener(Listener.ThreadedListener(it.createInstance(), threadPool)) }
    }

    /**
     * Creates a kryonet [Listener] based on the given class. This listener will invoke all static
     * functions that match the parameter pattern (com.esotericsoftware.kryonet.Connection; the incoming
     * packet).
     */
    private fun KClass<*>.createListener(): Listener = Listener.ThreadedListener(
        object : Listener() {
            val instance = this@createListener.createInstance()
            val functions = declaredFunctions.filter {
                it.hasAnnotation<PacketListener>()
            }.filter {
                val params = it.parameters.filter { param -> param.kind == KParameter.Kind.VALUE }
                params.size == 2 && params[0].type.jvmErasure == Connection::class
            }.groupBy {
                val params = it.parameters.filter { param -> param.kind == KParameter.Kind.VALUE }
                params[1].type.jvmErasure
            }

            override fun received(connection: Connection, incoming: Any) {
                try {
                    functions[incoming::class]?.forEach {
                        it.call(instance, connection, incoming)
                    }
                } catch (e: Throwable) {
                    LogManager.getLogger().error("Could not dispatch packet-receive to ${this@createListener.simpleName} " +
                            "for incoming packet $incoming on connection $connection: ", e)
                }
            }
        }, threadPool
    )
}
