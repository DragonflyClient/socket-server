package net.dragonfly.kernel.server.statistics

import com.mongodb.client.model.*
import kotlinx.coroutines.runBlocking
import net.dragonfly.kernel.server.Database
import net.dragonfly.kernel.server.session.Session
import org.apache.logging.log4j.LogManager
import java.text.SimpleDateFormat

object Statistics {

    fun updateOnlineTime(session: Session) = runBlocking {
        if (session.metadata["inactive"] == true) return@runBlocking
        val firstActiveTime = session.metadata["first_keep_active"] as? Long ?: return@runBlocking
        val lastKeepActive = session.metadata["last_keep_active"] as? Long ?: return@runBlocking

        val millis = lastKeepActive - firstActiveTime
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
