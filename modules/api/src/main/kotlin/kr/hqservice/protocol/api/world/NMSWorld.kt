package kr.hqservice.protocol.api.world

import kr.hqservice.protocol.api.NMSClass
import kr.hqservice.protocol.api.entity.NMSEntity
import org.bukkit.World
import org.bukkit.entity.Entity

interface NMSWorld : NMSClass<World> {
    fun getName(): String

    fun findEntity(id: Int): NMSEntity<out Entity>?
}