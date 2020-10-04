package net.dragonfly.kernel.server.utils

class RateLimit(
    val delay: Long,
    var lastHit: Long
)