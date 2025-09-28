package kr.hqservice.protocol.api.entity

import kr.hqservice.protocol.api.NMSClass
import kr.hqservice.protocol.api.world.NMSLocation
import org.bukkit.entity.Entity

interface NMSEntity<C : Entity> : NMSClass<C> {
    fun getId(): Int

    fun getLocation(): NMSLocation
}