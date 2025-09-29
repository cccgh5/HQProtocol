package kr.hqservice.protocol.v21_4.entity

import kr.hqservice.protocol.api.world.NMSLocation
import kr.hqservice.protocol.api.entity.NMSEntity
import kr.hqservice.protocol.v21_4.world.NMSLocationImpl
import kr.hqservice.protocol.v21_4.world.NMSWorldImpl
import net.minecraft.network.protocol.game.ClientboundTeleportEntityPacket
import net.minecraft.world.entity.Entity
import net.minecraft.world.entity.PositionMoveRotation
import org.bukkit.Bukkit
import org.bukkit.craftbukkit.entity.CraftPlayer
import org.bukkit.entity.Player
import org.bukkit.entity.Entity as CraftEntity

class NMSEntityImpl<T : CraftEntity>(
    private val entity: Entity,
) : NMSEntity<T> {
    override fun getId(): Int {
        return entity.id
    }

    override fun getLocation(): NMSLocation {
        return NMSLocationImpl(
            world = NMSWorldImpl(entity.level()),
            x = entity.x,
            y = entity.y,
            z = entity.z,
            yaw = entity.yRot,
            pitch = entity.xRot
        )
    }

    override fun teleport(
        location: NMSLocation,
        vararg viewer: Player
    ) {
        entity.setPos(location.getX(), location.getY(), location.getZ())
        entity.xRot = location.getPitch()
        entity.yRot = location.getYaw()
        val packet = ClientboundTeleportEntityPacket(entity.id, PositionMoveRotation.of(entity), setOf(), entity.onGround)
        viewer.forEach { (it as CraftPlayer).handle.connection.sendPacket(packet) }
    }

    override fun handle(): T {
        return Bukkit.getEntity(entity.uuid) as? T
            ?: throw NullPointerException("cannot found entity from world by uuid: ${entity.uuid}(${entity.id})")
    }
}