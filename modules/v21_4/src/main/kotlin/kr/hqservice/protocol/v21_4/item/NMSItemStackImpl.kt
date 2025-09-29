package kr.hqservice.protocol.v21_4.item

import io.papermc.paper.adventure.PaperAdventure
import kr.hqservice.protocol.api.item.NMSItemStack
import net.kyori.adventure.text.Component
import net.minecraft.core.component.DataComponents
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.component.ItemLore
import org.bukkit.Material
import org.bukkit.craftbukkit.inventory.CraftItemStack
import org.bukkit.inventory.ItemStack as BukkitItemStack

class NMSItemStackImpl(
    private val nmsInstance: ItemStack
) : NMSItemStack {
    override fun handle(): BukkitItemStack {
        return CraftItemStack.asBukkitCopy(nmsInstance)
    }

    fun getNMSItemStack(): ItemStack {
        return nmsInstance
    }

    override fun getBukkitType(): Material {
        return nmsInstance.asBukkitCopy().type
    }

    override fun getItemName(): Component? {
        val component = nmsInstance.get(DataComponents.ITEM_NAME) ?: return null
        return PaperAdventure.asAdventure(component)
    }

    override fun setItemName(component: Component?) {
        nmsInstance.set(DataComponents.ITEM_NAME, component?.let(PaperAdventure::asVanilla))
    }

    override fun getLore(): List<Component>? {
        val itemLore = nmsInstance.get(DataComponents.LORE) ?: return null
        return itemLore.lines.map { PaperAdventure.asAdventure(it) }
    }

    override fun setLore(components: List<Component>?) {
        val adventureComponents = components?.map { PaperAdventure.asVanilla(it) }
        nmsInstance.set(DataComponents.LORE, adventureComponents?.let { ItemLore(it) })
    }
}