package kr.hqservice.protocol.v21_4.world

import kr.hqservice.protocol.api.world.NMSWorld
import kr.hqservice.protocol.api.entity.NMSEntity
import kr.hqservice.protocol.v21_4.entity.NMSEntityImpl
import net.minecraft.world.level.Level
import org.bukkit.World
import org.bukkit.entity.Entity

class NMSWorldImpl(
    private val level: Level
) : NMSWorld {
    override fun getName(): String {
        return level.world.name
    }

    override fun findEntity(id: Int): NMSEntity<out Entity>? {
        return level.getEntity(id)?.let {
            NMSEntityImpl(it)
        }
    }

    override fun handle(): World {
        return level.world
    }
}