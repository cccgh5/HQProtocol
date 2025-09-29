package kr.hqservice.protocol.api.item

import kr.hqservice.protocol.api.NMS
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

interface NMSItemStack : NMS<ItemStack> {
    fun getBukkitType(): Material

    fun getItemName(): Component?

    fun setItemName(component: Component?)

    fun getLore(): List<Component>?

    fun setLore(components: List<Component>?)
}