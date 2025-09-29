package kr.hqservice.protocol.api.entity

import kr.hqservice.protocol.api.NMS
import kr.hqservice.protocol.api.world.NMSLocation
import org.bukkit.entity.Entity
import org.bukkit.entity.Player

interface NMSEntity<C : Entity> : NMS<C> {
    fun getId(): Int

    fun getLocation(): NMSLocation

    fun teleport(location: NMSLocation, vararg viewer: Player)
}