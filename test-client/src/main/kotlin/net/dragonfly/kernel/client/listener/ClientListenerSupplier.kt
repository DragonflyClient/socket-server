package net.dragonfly.kernel.client.listener

import com.esotericsoftware.kryonet.Listener
import net.dragonfly.kernel.collector.ListenerSupplier
import kotlin.reflect.KClass

object ClientListenerSupplier : ListenerSupplier {

    override fun getServerListeners(): Collection<KClass<out Listener>> = listOf()

    override fun getPacketListeners() = listOf(
        TestListener::class
    )
}