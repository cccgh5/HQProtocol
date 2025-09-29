package kr.hqservice.protocol.core.component

import kr.hqservice.framework.bukkit.core.HQBukkitPlugin
import kr.hqservice.protocol.api.component.HandlerPriority
import kr.hqservice.protocol.api.packet.NMSPacket
import java.util.logging.Level
import kotlin.reflect.KClass
import kotlin.reflect.KFunction
import kotlin.reflect.full.callSuspend

class PacketExecutor(
    private val packetClass: KClass<out NMSPacket>,
    private val listenerInstance: Any,
    private val method: KFunction<*>,
    private val priority: HandlerPriority,
    private val plugin: HQBukkitPlugin
) : Comparable<PacketExecutor> {
    suspend fun execute(packet: NMSPacket) {
        if (packetClass.isInstance(packet)) {
            invokeHandler(packet)
        }
    }

    private suspend fun invokeHandler(packet: NMSPacket) {
        try {
            method.callSuspend(listenerInstance, packet)
        } catch (exception: Exception) {
            val cause = exception.cause ?: exception
            plugin.logger.log(Level.WARNING, "Failed to execute packet handler method.", cause)
        }
    }

    override fun compareTo(other: PacketExecutor): Int {
        return priority.ordinal.compareTo(other.priority.ordinal)
    }
}