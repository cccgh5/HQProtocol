package kr.hqservice.protocol.v21_4.packet.client

import kr.hqservice.protocol.api.packet.client.NMSClientboundContainerSetDataPacket
import net.minecraft.network.protocol.game.ClientboundContainerSetDataPacket

class NMSClientboundContainerSetDataPacketImpl(
    original: ClientboundContainerSetDataPacket
) : NMSClientboundContainerSetDataPacket, NMSClientsidePacket<ClientboundContainerSetDataPacket>(original) {
    override fun getContainerId(): Int {
        return getCurrent().containerId
    }

    override fun getId(): Int {
        return getCurrent().id
    }

    override fun getValue(): Int {
        return getCurrent().value
    }
}