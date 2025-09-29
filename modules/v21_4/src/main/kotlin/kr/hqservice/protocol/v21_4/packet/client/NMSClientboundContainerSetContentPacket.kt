package kr.hqservice.protocol.v21_4.packet.client

import kr.hqservice.protocol.api.item.NMSItemStack
import kr.hqservice.protocol.api.packet.client.NMSClientboundContainerSetContentPacket
import kr.hqservice.protocol.v21_4.item.NMSItemStackImpl
import net.minecraft.core.NonNullList
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket
import net.minecraft.world.item.Items

class NMSClientboundContainerSetContentPacket(
    original: ClientboundContainerSetContentPacket
) : NMSClientboundContainerSetContentPacket, NMSClientsidePacket<ClientboundContainerSetContentPacket>(original) {
    override fun getContainerId(): Int {
        return getCurrent().containerId
    }

    override fun getStateId(): Int {
        return getCurrent().stateId
    }

    override fun getItems(): List<NMSItemStack> {
        return getCurrent().items.map { NMSItemStackImpl(it) }
    }

    override fun setItems(items: List<NMSItemStack>) {
        updateNext(
            ClientboundContainerSetContentPacket(
                getContainerId(),
                getStateId(),
                items.map { (it as NMSItemStackImpl).getNMSItemStack() }.let { NonNullList.of(Items.AIR.defaultInstance, *it.toTypedArray()) },
                getCarriedItem().let { (it as NMSItemStackImpl).getNMSItemStack() }
            )
        )
    }

    override fun getCarriedItem(): NMSItemStack {
        return NMSItemStackImpl(getCurrent().carriedItem)
    }

    override fun setCarriedItem(itemStack: NMSItemStack) {
        updateNext(
            ClientboundContainerSetContentPacket(
                getContainerId(),
                getStateId(),
                getItems().map { (it as NMSItemStackImpl).getNMSItemStack() }.let { NonNullList.of(Items.AIR.defaultInstance, *it.toTypedArray()) },
                (itemStack as NMSItemStackImpl).getNMSItemStack()
            )
        )
    }
}