package net.dragonfly.socket.server.statistics

import com.mongodb.client.model.*
import kotlinx.coroutines.runBlocking
import net.dragonfly.socket.server.Database
import net.dragonfly.socket.server.session.Session

object Statistics {

    fun updateOnlineTime(session: Session) = runBlocking {
        val firstActiveTime = session.metadata["first_active_time"] as? Long ?: session.createdAt
        val millis = System.currentTimeMillis() - firstActiveTime
        val time = millis / (1000 * 60) // in minutes

        Database.statisticsCollection.updateOne(
            Filters.eq("dragonflyUUID", session.account.uuid),
            Updates.inc("onlineTime", time),
            UpdateOptions().upsert(true)
        )
    }
}
