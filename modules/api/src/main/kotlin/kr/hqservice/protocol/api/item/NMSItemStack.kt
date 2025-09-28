package kr.hqservice.protocol.api.item

import kr.hqservice.protocol.api.NMSClass
import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.inventory.ItemStack

interface NMSItemStack : NMSClass<ItemStack> {
    fun getBukkitType(): Material

    fun getItemName(): Component?

    fun setItemName(component: Component?)

    fun getLore(): List<Component>?

    fun setLore(components: List<Component>?)
}