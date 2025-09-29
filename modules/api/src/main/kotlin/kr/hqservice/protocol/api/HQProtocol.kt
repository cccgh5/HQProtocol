package kr.hqservice.protocol.api

import kr.hqservice.protocol.api.entity.NMSEntity
import org.bukkit.Location
import org.bukkit.entity.Player
import kotlin.reflect.KClass

interface HQProtocol {
    fun <C : Any, T : NMS<C>> wrapNMS(craftInstance: C, nmsClass: KClass<T>): T?

    fun <T : NMSEntity<*>> summonNMSEntity(nmsEntityClass: KClass<T>, location: Location, vararg viewer: Player): T?
}