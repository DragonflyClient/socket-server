package net.dragonfly.socket.server.statistics

import com.mongodb.client.model.*
import kotlinx.coroutines.runBlocking
import net.dragonfly.socket.server.Database
import net.dragonfly.socket.server.session.Session
import java.text.SimpleDateFormat

object Statistics {

    fun updateOnlineTime(session: Session) = runBlocking {
        val firstActiveTime = session.metadata["first_active_time"] as? Long ?: session.createdAt
        val millis = System.currentTimeMillis() - firstActiveTime
        val time = millis / (1000 * 60) // in minutes

        val sdf = SimpleDateFormat("MM/yyyy")
        val month = sdf.format(System.currentTimeMillis())

        Database.statisticsCollection.updateOne(
            Filters.eq("dragonflyUUID", session.account.uuid),
            Updates.combine(
                Updates.inc("onlineTime.total", time),
                Updates.inc("onlineTime.$month", time)
            ),
            UpdateOptions().upsert(true)
        )
    }
}
