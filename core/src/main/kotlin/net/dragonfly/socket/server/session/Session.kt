package net.dragonfly.socket.server.session

import com.esotericsoftware.kryonet.Connection

class Session(
    val connection: Connection,
    val jwt: String
) {
    val createdAt: Long = System.currentTimeMillis()
    val metadata = mutableMapOf<String, Any?>()

    override fun toString(): String {
        return "Session(connection=$connection, jwt='$jwt', createdAt=$createdAt, metadata=$metadata)"
    }
}
