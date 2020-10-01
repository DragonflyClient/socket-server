package net.dragonfly.socket.server.session

import com.esotericsoftware.kryonet.Connection
import khttp.post
import net.dragonfly.library.DragonflyLibrary

/**
 * Manages the creation and the retrieval of sessions.
 */
object SessionManager {

    /**
     * Storage for all connections along with their sessions.
     */
    private val sessions = mutableMapOf<Connection, Session>()

    /**
     * Creates a new [Session] for the given [connection] by authenticating using the
     * given [jwt]. Returns null if the authentication failed.
     */
    fun buildSession(connection: Connection, jwt: String): Session? {
        try {
            val response = DragonflyLibrary.authentication.startRequest()
                .withToken(jwt)
                .execute()
                .getResponse() ?: return null

            val username = response.username
            val uuid = response.uuid
            connection.setName("$username#$uuid")

            val session = Session(connection, jwt, response)
            sessions[connection] = session
            return session
        } catch (e: Throwable) {
            e.printStackTrace()
            return null
        }
    }

    /**
     * Finds a session by it's [connection] and returns null if none is found.
     */
    fun findSession(connection: Connection) = sessions[connection]

    /**
     * Convenient access to the [Session] of a [Connection] that throws an error if no session
     * is set.
     */
    val Connection.session: Session
        get() = findSession(this) ?: error("Socket connection has no associated session!")
}
