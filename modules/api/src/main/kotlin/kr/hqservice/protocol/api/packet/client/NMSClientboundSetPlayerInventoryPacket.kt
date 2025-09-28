package kr.hqservice.protocol.api.packet.client

import kr.hqservice.protocol.api.item.NMSItemStack
import kr.hqservice.protocol.api.packet.NMSPacket

interface NMSClientboundSetPlayerInventoryPacket : NMSPacket {
    fun getSlot(): Int

    fun getItemStack(): NMSItemStack
}