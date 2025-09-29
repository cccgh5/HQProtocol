package kr.hqservice.protocol.core.listener

import kr.hqservice.protocol.api.component.PacketListener
import kr.hqservice.protocol.api.component.Subscribe
import kr.hqservice.protocol.api.packet.client.NMSClientboundContainerSetContentPacket
import kr.hqservice.protocol.api.packet.client.NMSClientboundContainerSetSlotPacket
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.Style
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material

@PacketListener
class ClientsideItemSetter {
    @Subscribe
    fun testPacketReceived(packet: NMSClientboundContainerSetSlotPacket) {
        packet.getItemStack().apply {
            setItemName(Component.text("Modified ItemName in Clientside"))
            setLore(
                listOf(
                    Component.text("line 1").style(Style.style(
                        TextColor.color(255,55,55),
                        TextDecoration.ITALIC.withState(false)
                    ))
                )
            )
        }
    }

    @Subscribe
    fun testPacketReceived(packet: NMSClientboundContainerSetContentPacket) {
        packet.getItems().apply {
            forEach {
                if (it.getBukkitType() != Material.AIR) {
                    it.setItemName(Component.text("Modified ItemName in Clientside"))
                    it.setLore(
                        listOf(
                            Component.text("line 1").style(
                                Style.style(
                                    TextColor.color(255, 55, 55),
                                    TextDecoration.ITALIC.withState(false)
                                )
                            )
                        )
                    )
                }
            }
        }
    }
}