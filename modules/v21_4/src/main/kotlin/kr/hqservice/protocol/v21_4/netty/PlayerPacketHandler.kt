package kr.hqservice.protocol.v21_4.netty

import io.netty.channel.ChannelDuplexHandler
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelPromise
import io.papermc.paper.configuration.GlobalConfiguration
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.launch
import kr.hqservice.framework.bukkit.core.HQBukkitPlugin
import kr.hqservice.protocol.core.component.registry.PacketHandlerRegistry
import kr.hqservice.protocol.v21_4.HQProtocolImpl
import net.minecraft.core.UUIDUtil
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.login.ServerboundHelloPacket
import java.util.UUID

class PlayerPacketHandler(
    private val plugin: HQBukkitPlugin,
    private val protocol: HQProtocolImpl,
    private val packetHandlerRegistry: PacketHandlerRegistry,
) : ChannelDuplexHandler() {
    private lateinit var uniqueId: UUID

    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        if (msg is ServerboundHelloPacket) {
            uniqueId = if (plugin.server.onlineMode || GlobalConfiguration.get().proxies.velocity.enabled) msg.profileId
            else UUIDUtil.createOfflinePlayerUUID(msg.name)
        }
        /*if (msg is Packet<*>) {
            if (msg is ClientboundContainerSetSlotPacket) {
                println("hi")
            }
        }*/

        super.channelRead(ctx, msg)
    }

    override fun write(ctx: ChannelHandlerContext, msg: Any, promise: ChannelPromise) {
        if (msg !is Packet<*>) return super.write(ctx, msg, promise)

        plugin.launch(start = CoroutineStart.UNDISPATCHED) {
            val executePacketEvent: suspend (Packet<*>) -> Boolean? = func@{ packet ->
                val wrapper = protocol.makeWrapper(packet)
                if (wrapper != null) {
                    val executors = packetHandlerRegistry.findExecutors(wrapper)
                    if (executors.isNotEmpty()) {
                        for (executor in executors) {
                            runCatching {
                                executor.execute(wrapper)
                            }.onFailure { throwable ->
                                promise.tryFailureOnEventLoop(ctx, throwable)
                                return@func true
                            }
                        }
                    }

                    if (wrapper.isCancelled()) true else false
                } else null
            }

            if (executePacketEvent(msg) == true) {
                if (!promise.isDone) promise.trySuccessOnEventLoop(ctx)
                return@launch
            }

            val extraPackets = msg.extraPackets?.toList()
            if (extraPackets != null) {
                for (packet in extraPackets) {
                    println("sss")
                    if (executePacketEvent(packet) == true) {
                        msg.extraPackets?.remove(packet)
                    }
                }
            }

            ctx.ensureOnEventLoop {
                super.write(ctx, msg, promise)
            }
        }
    }
}