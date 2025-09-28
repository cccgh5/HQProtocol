package kr.hqservice.protocol.api.packet.client

import kr.hqservice.protocol.api.packet.NMSPacket

interface NMSClientboundContainerSetDataPacket : NMSPacket {
    fun getContainerId(): Int

    fun getId(): Int

    fun getValue(): Int
}