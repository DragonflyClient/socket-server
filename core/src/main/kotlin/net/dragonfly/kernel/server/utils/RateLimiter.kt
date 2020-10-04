package net.dragonfly.kernel.server.utils

/**
 * A simple utility class that is responsible for applying delays to some actions.
 */
object RateLimiter {

    /**
     * Storage for all available rate limits.
     */
    private val rateLimits = mutableMapOf<String, RateLimit>()

    /**
     * Returns whether the rate limit with the given [id] has expired and thus can
     * be consumed again.
     */
    fun isExpired(id: String): Boolean {
        val item = rateLimits[id] ?: return true
        return item.lastHit + item.delay < System.currentTimeMillis()
    }

    /**
     * Hits the rate limit with the given [id] at the current time which makes the
     * delay begin again.
     */
    fun hit(id: String) {
        rateLimits[id]?.lastHit = System.currentTimeMillis()
    }

    /**
     * Creates a new rate limit with the specified [id] and [delay]. This function
     * does not hit the limit which makes it [expired][isExpired] by default.
     */
    fun create(id: String, delay: Long) = rateLimits.put(id, RateLimit(delay, 0L))

    /**
     * [Creates][create] and [hits][hit] the rate limit which makes it unavailable
     * after creation.
     */
    fun createAndHit(id: String, delay: Long) {
        create(id, delay)
        hit(id)
    }

    /**
     * Consumes the rate limit with the given [id] and [delay]. This means that if it is
     * [expired][isExpired] it will be [hit] and this function returns true. If it is not
     * expired, this function will do nothing and return false.
     */
    fun consume(id: String, delay: Long): Boolean = if (isExpired(id)) {
        createAndHit(id, delay)
        true
    } else false

    /**
     * Convenient function for executing the given [block] if the rate limit can be
     * [consumed][consume].
     */
    fun consume(id: String, delay: Long, block: () -> Unit) {
        if (consume(id, delay)) {
            block()
        }
    }
}