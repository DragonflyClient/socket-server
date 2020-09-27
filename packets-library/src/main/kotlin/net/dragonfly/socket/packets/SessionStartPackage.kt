package net.dragonfly.socket.packets

class SessionStartPackage @JvmOverloads constructor(
    val jwt: String? = null
) : Packet
