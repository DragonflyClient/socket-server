package net.dragonfly.kernel.packets.client

import net.dragonfly.kernel.packets.Packet

/**
 * Sent by the client to tell the server that it isn't inactive.
 */
class KeepActivePacket : Packet {
    companion object {
        const val serialVersionUID = 32L
    }
}
