package kr.hqservice.protocol.core.component.registry

import kr.hqservice.framework.global.core.component.Bean
import kr.hqservice.protocol.api.packet.NMSPacket
import kr.hqservice.protocol.core.component.PacketExecutor
import kotlin.reflect.KClass

@Bean
class PacketHandlerRegistry {
    private val executors = mutableMapOf<KClass<out NMSPacket>, MutableList<PacketExecutor>>()

    fun registerExecutors(packetExecutors: Map<KClass<out NMSPacket>, MutableList<PacketExecutor>>) {
        packetExecutors.forEach { (packetClass, executors) ->
            val existingExecutors = this.executors[packetClass]
            if (existingExecutors == null) {
                this.executors[packetClass] = executors
            } else {
                existingExecutors.addAll(executors)
            }
        }

        executors.forEach { (_, executors) -> executors.sort() }
    }

    fun findExecutors(packet: NMSPacket): List<PacketExecutor> {
        return executors.entries.filter {
            it.key.isInstance(packet)
        }.map { it.value }.flatten()
    }
}