package net.dragonfly.kernel.server.session

import com.esotericsoftware.kryonet.Connection
import net.dragonfly.library.authentication.AuthenticationResponse

/**
 * An instance of this class is associated with every connection once it has started
 * to identify the client behind it.
 *
 * @param connection The connection that this session belongs to
 * @param jwt The Dragonfly token that the connection used for authentication
 * @param account The account that has been responded by the authentication process
 */
data class Session(
    val connection: Connection,
    val jwt: String,
    val account: AuthenticationResponse
) {

    /**
     * When the session was created
     */
    val createdAt: Long = System.currentTimeMillis()

    /**
     * Metadata that can be associated with the session to provide state.
     */
    val metadata = mutableMapOf<String, Any?>()

    override fun toString(): String {
        return "Session(connection=$connection, jwt='$jwt', account=$account, createdAt=$createdAt, metadata=$metadata)"
    }
}
