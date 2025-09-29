package kr.hqservice.protocol.v21_4.packet.client

import kr.hqservice.protocol.api.item.NMSItemStack
import kr.hqservice.protocol.api.packet.client.NMSClientboundContainerSetSlotPacket
import kr.hqservice.protocol.v21_4.item.NMSItemStackImpl
import net.minecraft.network.protocol.game.ClientboundContainerSetSlotPacket

class NMSClientboundContainerSetSlotPacketImpl(
    original: ClientboundContainerSetSlotPacket
) : NMSClientboundContainerSetSlotPacket, NMSClientsidePacket<ClientboundContainerSetSlotPacket>(original) {
    override fun getContainerId(): Int {
        return getCurrent().containerId
    }

    override fun getStateId(): Int {
        return getCurrent().stateId
    }

    override fun getSlot(): Int {
        return getCurrent().slot
    }

    override fun setSlot(slot: Int) {
        updateNext(
            ClientboundContainerSetSlotPacket(
                getContainerId(),
                getStateId(),
                slot,
                (getItemStack() as NMSItemStackImpl).getNMSItemStack()
            )
        )
    }

    override fun getItemStack(): NMSItemStack {
        return NMSItemStackImpl(getCurrent().item)
    }

    override fun setItemStack(itemStack: NMSItemStack) {
        updateNext(
            ClientboundContainerSetSlotPacket(
                getContainerId(),
                getStateId(),
                getSlot(),
                (itemStack as NMSItemStackImpl).getNMSItemStack()
            )
        )
    }
}