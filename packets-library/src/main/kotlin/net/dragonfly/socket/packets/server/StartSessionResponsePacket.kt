package net.dragonfly.socket.packets.server

import net.dragonfly.socket.packets.Packet

class StartSessionResponsePacket @JvmOverloads constructor(
    val success: Boolean? = null
) : Packet
