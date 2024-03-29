package net.dragonfly.kernel.packets.client

import net.dragonfly.kernel.packets.Packet

/**
 * Sent from the client to the server to show that it is still "alive".
 */
class KeepAlivePacket : Packet {
    companion object {
        const val serialVersionUID = 48L
    }
}
