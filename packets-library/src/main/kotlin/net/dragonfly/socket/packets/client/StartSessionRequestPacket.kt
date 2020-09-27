package net.dragonfly.socket.packets.client

import net.dragonfly.socket.packets.Packet

class StartSessionRequestPacket @JvmOverloads constructor(
    val jwt: String? = null
) : Packet
