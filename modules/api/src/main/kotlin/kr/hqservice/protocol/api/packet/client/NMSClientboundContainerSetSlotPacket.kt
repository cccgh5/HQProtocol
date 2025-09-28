package kr.hqservice.protocol.api.packet.client

import kr.hqservice.protocol.api.item.NMSItemStack
import kr.hqservice.protocol.api.packet.NMSPacket

interface NMSClientboundContainerSetSlotPacket : NMSPacket {
    fun getContainerId(): Int

    fun getStateId(): Int

    fun getSlot(): Int

    fun setSlot(slot: Int)

    fun getItemStack(): NMSItemStack

    fun setItemStack(itemStack: NMSItemStack)
}