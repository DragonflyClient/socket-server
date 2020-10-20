package net.dragonfly.kernel.server

import net.dragonfly.secrets.CONNECTION_STRING
import org.bson.Document
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

object Database {
    val client = KMongo.createClient(CONNECTION_STRING).coroutine
    val database = client.getDatabase("dragonfly")
    val statisticsCollection = database.getCollection<Document>("statistics")
    val accountsCollection = database.getCollection<Document>("accounts")
}
