package kr.hqservice.protocol.v21_4.packet.client

import kr.hqservice.protocol.api.item.NMSItemStack
import kr.hqservice.protocol.api.packet.client.NMSClientboundSetPlayerInventoryPacket
import kr.hqservice.protocol.v21_4.item.NMSItemStackImpl
import net.minecraft.network.protocol.game.ClientboundSetPlayerInventoryPacket

class NMSClientboundSetPlayerInventoryPacketImpl(
    original: ClientboundSetPlayerInventoryPacket
) : NMSClientboundSetPlayerInventoryPacket, NMSClientsidePacket<ClientboundSetPlayerInventoryPacket>(original) {
    override fun getSlot(): Int {
        return getCurrent().slot
    }

    override fun getItemStack(): NMSItemStack {
        return NMSItemStackImpl(getCurrent().contents)
    }
}