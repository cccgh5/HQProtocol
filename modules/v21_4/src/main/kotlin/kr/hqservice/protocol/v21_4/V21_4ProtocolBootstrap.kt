package kr.hqservice.protocol.v21_4

import kr.hqservice.framework.bukkit.core.component.module.Module
import kr.hqservice.framework.bukkit.core.component.module.Setup
import kr.hqservice.protocol.v21_4.packet.client.NMSClientsidePacket
import net.minecraft.network.protocol.Packet
import kotlin.reflect.KClass
import kotlin.reflect.full.primaryConstructor

@Module
class V21_4ProtocolBootstrap(
    private val protocol: HQProtocolImpl
) {
    @Setup
    fun setup() {
        val clientSidePacketClasses = NMSClientsidePacket::class.sealedSubclasses
        clientSidePacketClasses
            .asSequence()
            .filter { it.objectInstance == null && !it.isAbstract }
            .forEach { kClass ->
                val constructor = kClass.primaryConstructor ?: kClass.constructors.firstOrNull() ?: return@forEach
                val param = constructor.parameters.firstOrNull() ?: return@forEach
                val packetKClass = (param.type.classifier as? KClass<*>) ?: return@forEach

                val packetJava = packetKClass.java
                if (!Packet::class.java.isAssignableFrom(packetJava)) return@forEach

                protocol.registerWrapper(packetJava as Class<Packet<*>>) {
                    constructor.call(it)
                }
            }
    }
}