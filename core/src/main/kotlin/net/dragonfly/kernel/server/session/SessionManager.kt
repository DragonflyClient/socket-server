package net.dragonfly.kernel.server.session

import com.esotericsoftware.kryonet.Connection
import net.dragonfly.kernel.server.users.UsersManagement
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

            // check if account is already used in a session
            if (sessions.values.any { it.account.uuid == uuid }) return null

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
     * Ends the session of the given [connection] by removing it from the [sessions]
     * storage.
     */
    fun endSession(connection: Connection) {
        val session = sessions.remove(connection) ?: return

        UsersManagement.endSession(session)
    }

    /**
     * Convenient access to the [Session] of a [Connection] that throws an error if no session
     * is set.
     */
    val Connection.session: Session
        get() = sessionOrNull ?: error("Socket connection has no associated session!")

    /**
     * Convenient access to the [Session] of a [Connection] or null if no session has been set.
     */
    val Connection.sessionOrNull: Session?
        get() = findSession(this)
}
