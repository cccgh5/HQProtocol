package kr.hqservice.protocol.v21_4

import com.mojang.authlib.GameProfile
import com.mojang.authlib.properties.Property
import io.papermc.paper.network.ChannelInitializeListenerHolder
import kr.hqservice.framework.bukkit.core.HQBukkitPlugin
import kr.hqservice.framework.bukkit.core.util.PluginScopeFinder
import kr.hqservice.framework.global.core.component.Service
import kr.hqservice.protocol.api.NMS
import kr.hqservice.protocol.api.entity.NMSArmorStand
import kotlin.reflect.KClass
import kr.hqservice.protocol.api.entity.NMSEntity
import kr.hqservice.protocol.api.entity.NMSPlayer
import kr.hqservice.protocol.api.world.NMSLocation
import kr.hqservice.protocol.api.world.NMSWorld
import kr.hqservice.protocol.core.IHQProtocol
import kr.hqservice.protocol.core.component.registry.PacketHandlerRegistry
import kr.hqservice.protocol.v21_4.entity.NMSArmorStandImpl
import kr.hqservice.protocol.v21_4.entity.NMSEntityImpl
import kr.hqservice.protocol.v21_4.entity.NMSPlayerImpl
import kr.hqservice.protocol.v21_4.entity.player.FakePlayerConnection
import kr.hqservice.protocol.v21_4.entity.player.FakePlayerNetworkHandler
import kr.hqservice.protocol.v21_4.netty.PlayerPacketHandler
import kr.hqservice.protocol.v21_4.packet.NMSPacketImpl
import kr.hqservice.protocol.v21_4.world.NMSLocationImpl
import kr.hqservice.protocol.v21_4.world.NMSWorldImpl
import net.kyori.adventure.key.Key
import net.minecraft.network.protocol.Packet
import net.minecraft.network.protocol.PacketFlow
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket
import net.minecraft.server.level.ClientInformation
import net.minecraft.server.level.ParticleStatus
import net.minecraft.server.level.ServerEntity
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.player.ChatVisiblity
import org.bukkit.Location
import org.bukkit.craftbukkit.CraftWorld
import org.bukkit.craftbukkit.entity.CraftEntity
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import java.util.UUID

@Service
class HQProtocolImpl(
    private val plugin: Plugin,
    private val packetHandlerRegistry: PacketHandlerRegistry
) : IHQProtocol {
    private val packetFactories: MutableMap<Class<out Packet<*>>, (Packet<*>) -> NMSPacketImpl<*>> = linkedMapOf()

    fun <P : Packet<*>> registerWrapper(
        packetClass: Class<P>,
        factory: (P) -> NMSPacketImpl<out P>
    ) {
        packetFactories[packetClass] = { packet -> factory(packet as P) }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Packet<*>> makeWrapper(packet: T): NMSPacketImpl<T>? {
        packetFactories[packet.javaClass]?.let { return it(packet) as NMSPacketImpl<T> }
        val entry = packetFactories.entries.firstOrNull { (k, _) -> k.isInstance(packet) } ?: return null
        return entry.value(packet) as NMSPacketImpl<T>
    }

    @Suppress("UNCHECKED_CAST")
    override fun <T : NMSEntity<*>> summonNMSEntity(nmsEntityClass: KClass<T>, location: Location, vararg viewer: Player): T? {
        val sendPacket: (Packet<*>) -> Unit = { packet ->
            viewer.forEach {
                val handle = (it as CraftPlayer).handle
                handle.connection.send(packet)
            }
        }

        val level = (location.world as CraftWorld).handle
        return when (nmsEntityClass) {
            NMSPlayer::class -> {
                val profile = GameProfile(UUID.randomUUID(), "FakePlayer")
                val serverPlayer = ServerPlayer(level.server, level, profile, ClientInformation.createDefault())
                serverPlayer.setPos(location.x, location.y, location.z)

                val npcServerEntity = ServerEntity(level, serverPlayer, 0, false, {}, setOf())
                val connection = FakePlayerNetworkHandler(level.server, FakePlayerConnection(PacketFlow.CLIENTBOUND), serverPlayer)
                serverPlayer.connection = connection

                val wrapper = NMSPlayerImpl(serverPlayer)
                val updatePacket = ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, serverPlayer)
                sendPacket(updatePacket)

                val addPacket = ClientboundAddEntityPacket(serverPlayer, npcServerEntity)
                sendPacket(addPacket)

                wrapper as T
            }
            NMSArmorStand::class -> {
                val nmsArmorStand = net.minecraft.world.entity.decoration.ArmorStand(level, location.x, location.y, location.z)
                val npcServerEntity = ServerEntity(level, nmsArmorStand, 0, false, { }, setOf())

                val wrapper = NMSArmorStandImpl(nmsArmorStand)
                val addPacket = ClientboundAddEntityPacket(nmsArmorStand, npcServerEntity)
                sendPacket(addPacket)

                wrapper as T
            }
            NMSEntity::class -> {
                // val nms = (craftInstance as CraftEntity).handle
                // NMSEntityImpl<Entity>(nms) as NMS<C>
                null
            }
            else -> null
        }
    }

    @Suppress("UNCHECKED_CAST")
    override fun <C : Any, T : NMS<C>> wrapNMS(
        craftInstance: C,
        nmsClass: KClass<T>
    ): T? {
        return when (nmsClass) {
            NMSPlayer::class -> {
                val player = (craftInstance as? Player) ?: return null
                NMSPlayerImpl((player as CraftPlayer).handle) as T
            }
            NMSEntity::class -> {
                val entity = (craftInstance as? Entity) ?: return null
                val nms = (entity as CraftEntity).handle
                NMSEntityImpl<Entity>(nms) as T
            }
            NMSWorld::class -> {
                val world = (craftInstance as? org.bukkit.World) ?: return null
                NMSWorldImpl((world as CraftWorld).handle) as T
            }
            NMSLocation::class -> {
                craftInstance as Location
                NMSLocationImpl(wrapNMS(craftInstance.world!!, NMSWorld::class) as NMSWorldImpl, craftInstance.x, craftInstance.y, craftInstance.z, craftInstance.yaw, craftInstance.pitch) as T
            }
            else -> null
        }
    }

    override fun injectHandler(plugin: HQBukkitPlugin) {
        ChannelInitializeListenerHolder.addListener(Key.key("hqprotocol:packet-handler-pipeline-hook")) { channel ->
            val pipeline = channel.pipeline()
            if (pipeline.get("hqprotocol_handler") == null) {
                pipeline.addBefore(
                    "packet_handler", "hqprotocol_handler",
                    PlayerPacketHandler(plugin, this, packetHandlerRegistry)
                )
            }
        }
    }
}