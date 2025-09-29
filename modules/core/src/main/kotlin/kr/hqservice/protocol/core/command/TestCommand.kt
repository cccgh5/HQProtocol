package kr.hqservice.protocol.core.command

import kr.hqservice.framework.command.Command
import kr.hqservice.framework.command.CommandExecutor
import kr.hqservice.protocol.api.HQProtocol
import kr.hqservice.protocol.api.entity.NMSArmorStand
import kr.hqservice.protocol.api.entity.NMSEntity
import kr.hqservice.protocol.api.entity.NMSPlayer
import kr.hqservice.protocol.api.world.NMSLocation
import org.bukkit.entity.Entity
import org.bukkit.entity.Player
import kotlin.reflect.KClass

@Command(label = "test")
class TestCommand(
    private val protocol: HQProtocol
) {
    private var nmsEntity: NMSEntity<*>? = null

    @CommandExecutor("look")
    fun lookAt(sender: Player) {
        val lookAtEntity = sender.getTargetEntity(10) ?: return
        nmsEntity = protocol.wrapNMS(lookAtEntity,
            NMSEntity::class as KClass<NMSEntity<Entity>>
        ) ?: return

        sender.sendMessage("selected")
    }

    @CommandExecutor(
        label = "player"
    ) fun summon(sender: Player) {
        nmsEntity = protocol.summonNMSEntity(NMSPlayer::class, sender.location, sender)
    }

    @CommandExecutor("armorstand")
    fun summon2(sender: Player) {
        nmsEntity = protocol.summonNMSEntity(NMSArmorStand::class, sender.location, sender)
    }

    @CommandExecutor("tp")
    fun teleport(sender: Player) {
        nmsEntity?.teleport(protocol.wrapNMS(sender.location, NMSLocation::class)!!, sender)
    }
}