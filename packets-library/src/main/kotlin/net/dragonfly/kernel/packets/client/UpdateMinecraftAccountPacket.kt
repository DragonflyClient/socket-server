package net.dragonfly.kernel.packets.client

import net.dragonfly.kernel.packets.Packet

/**
 * Sent from the client to the server to tell that the Dragonfly user has changed the
 * Minecraft account he is currently playing on. This packet is also sent after the connection
 * has been established to tell the server about the current Minecraft account.
 *
 * @param currentMinecraftAccount The new Minecraft account
 */
class UpdateMinecraftAccountPacket @JvmOverloads constructor(
    val currentMinecraftAccount: String? = null
) : Packet
