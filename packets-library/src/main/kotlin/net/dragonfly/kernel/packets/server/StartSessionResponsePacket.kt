package net.dragonfly.kernel.packets.server

import net.dragonfly.kernel.packets.Packet

class StartSessionResponsePacket @JvmOverloads constructor(
    val success: Boolean? = null
) : Packet
