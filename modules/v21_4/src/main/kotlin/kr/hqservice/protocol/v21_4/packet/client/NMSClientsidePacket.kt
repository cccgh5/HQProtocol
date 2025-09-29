package kr.hqservice.protocol.v21_4.packet.client

import kr.hqservice.protocol.v21_4.packet.NMSPacketImpl
import net.minecraft.network.protocol.Packet

sealed class NMSClientsidePacket<T : Packet<*>>(
    original: T
) : NMSPacketImpl<T>(original)