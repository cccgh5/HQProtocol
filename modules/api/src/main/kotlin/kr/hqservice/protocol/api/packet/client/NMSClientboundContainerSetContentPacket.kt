package kr.hqservice.protocol.api.packet.client

import kr.hqservice.protocol.api.item.NMSItemStack
import kr.hqservice.protocol.api.packet.NMSPacket

interface NMSClientboundContainerSetContentPacket : NMSPacket {
    fun getContainerId(): Int

    fun getStateId(): Int

    fun getItems(): List<NMSItemStack>

    fun setItems(items: List<NMSItemStack>)

    fun getCarriedItem(): NMSItemStack

    fun setCarriedItem(itemStack: NMSItemStack)
}