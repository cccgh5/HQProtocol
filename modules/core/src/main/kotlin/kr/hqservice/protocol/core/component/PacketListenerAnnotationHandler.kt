package kr.hqservice.protocol.core.component

import kr.hqservice.framework.bukkit.core.HQBukkitPlugin
import kr.hqservice.framework.global.core.component.handler.AnnotationHandler
import kr.hqservice.framework.global.core.component.handler.HQAnnotationHandler
import kr.hqservice.protocol.api.component.PacketListener
import kr.hqservice.protocol.api.component.Subscribe
import kr.hqservice.protocol.api.packet.NMSPacket
import kr.hqservice.protocol.core.component.registry.PacketHandlerRegistry
import kotlin.reflect.KClass
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.full.hasAnnotation
import kotlin.reflect.full.isSubclassOf
import kotlin.reflect.jvm.isAccessible
import kotlin.reflect.jvm.javaMethod

@AnnotationHandler
class PacketListenerAnnotationHandler(
    private val plugin: HQBukkitPlugin,
    private val packetHandlerRegistry: PacketHandlerRegistry,
) : HQAnnotationHandler<PacketListener> {
    override fun setup(instance: Any, annotation: PacketListener) {
        val executors = createExecutors(instance)
        packetHandlerRegistry.registerExecutors(executors)
    }

    @Suppress("unchecked_cast")
    private fun createExecutors(listenerInstance: Any): Map<KClass<out NMSPacket>, MutableList<PacketExecutor>> {
        return listenerInstance::class.declaredFunctions
            .filter { it.hasAnnotation<Subscribe>() }
            .filter { it.javaMethod?.isBridge != true && it.javaMethod?.isSynthetic != true }
            .onEach { if (!it.isAccessible) it.isAccessible = true }
            .groupBy {
                val firstParameter = it.javaMethod!!.parameters.firstOrNull()?.type?.kotlin
                if (firstParameter?.isSubclassOf(NMSPacket::class) == false)
                    throw IllegalStateException("first parameter of packet handler must be NMSPacket.")
                firstParameter!!
            }
            .mapKeys { it.key as KClass<out NMSPacket>}
            .mapValues { (packet, functions) ->
                functions.map { kFunction ->
                    val executor = PacketExecutor(
                        packet,
                        listenerInstance,
                        kFunction,
                        kFunction.findAnnotation<Subscribe>()!!.priority,
                        plugin
                    )

                    executor
                }.toMutableList()
            }
    }
}