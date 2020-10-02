package net.dragonfly.kernel.collector

/**
 * Classes annotated with this annotation can have their static functions
 * called by a packet listener.
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
annotation class PacketListener
