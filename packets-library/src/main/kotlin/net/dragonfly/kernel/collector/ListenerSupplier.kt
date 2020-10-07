package net.dragonfly.kernel.collector

import com.esotericsoftware.kryonet.Listener
import kotlin.reflect.KClass

/**
 * A simple interface dedicated to supply the listeners for the endpoint.
 */
interface ListenerSupplier {

    /**
     * Returns all general listener instances.
     */
    fun getServerListeners(): Collection<KClass<out Listener>>

    /**
     * Returns all packet listeners that can be any class that contain functions with the dedicated
     * function parameter structure.
     */
    fun getPacketListeners(): Collection<KClass<*>>
}