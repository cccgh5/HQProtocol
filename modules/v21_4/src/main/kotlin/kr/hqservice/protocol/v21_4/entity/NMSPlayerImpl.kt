package kr.hqservice.protocol.v21_4.entity

import kr.hqservice.protocol.api.entity.NMSEntity
import kr.hqservice.protocol.api.entity.NMSPlayer
import net.minecraft.world.entity.player.Player
import org.bukkit.entity.Player as CraftPlayer

class NMSPlayerImpl(
    private val entity: Player,
) : NMSEntity<CraftPlayer> by NMSEntityImpl(entity), NMSPlayer {

}