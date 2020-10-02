package net.dragonfly.kernel.packets.client

import net.dragonfly.kernel.packets.Packet

class StartSessionRequestPacket @JvmOverloads constructor(
    val jwt: String? = null
) : Packet
