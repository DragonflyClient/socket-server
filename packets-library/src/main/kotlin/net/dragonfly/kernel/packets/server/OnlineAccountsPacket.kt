package net.dragonfly.kernel.packets.server

import net.dragonfly.kernel.packets.Packet

class OnlineAccountsPacket @JvmOverloads constructor(
    val onlineAccounts: Array<String>? = null
) : Packet