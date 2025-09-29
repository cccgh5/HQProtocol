package kr.hqservice.protocol.v21_4.entity

import kr.hqservice.protocol.api.entity.NMSArmorStand
import kr.hqservice.protocol.api.entity.NMSEntity
import net.minecraft.world.entity.decoration.ArmorStand

class NMSArmorStandImpl(
    private val entity: ArmorStand,
) : NMSEntity<org.bukkit.entity.ArmorStand> by NMSEntityImpl(entity), NMSArmorStand {

}