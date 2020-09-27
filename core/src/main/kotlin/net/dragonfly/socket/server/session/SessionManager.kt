package net.dragonfly.socket.server.session

import com.esotericsoftware.kryonet.Connection
import khttp.post

object SessionManager {

    private val sessions = mutableMapOf<Connection, Session>()

    fun buildSession(connection: Connection, jwt: String): Session? = try {
        val account = post(
            url = "https://api.playdragonfly.net/v1/authentication/token",
            headers = mapOf(
                "Authorization" to "Bearer $jwt"
            )
        ).jsonObject
        val username = account.getString("username")
        val uuid = account.getString("uuid")
        connection.setName("$username@$uuid")

        val session = Session(connection, jwt)
        sessions[connection] = session
        session.metadata["username"] = username
        session
    } catch (e: Throwable) {
        e.printStackTrace()
        null
    }

    fun findSession(connection: Connection) = sessions[connection]

    val Connection.session: Session
        get() = findSession(this) ?: error("Socket connection has no associated session!")
}
