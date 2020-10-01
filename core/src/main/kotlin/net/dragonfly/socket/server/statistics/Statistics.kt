package net.dragonfly.socket.server.statistics

import com.mongodb.client.model.*
import kotlinx.coroutines.runBlocking
import net.dragonfly.socket.server.Database
import net.dragonfly.socket.server.session.Session

object Statistics {

    fun updateOnlineTime(session: Session) = runBlocking {
        val time = (System.currentTimeMillis() - session.createdAt) / (1000 * 60) // in minutes
        Database.statisticsCollection.updateOne(
            Filters.eq("dragonflyUUID", session.account.uuid),
            Updates.inc("onlineTime", time),
            UpdateOptions().upsert(true)
        )
    }
}
