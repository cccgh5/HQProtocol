package kr.hqservice.protocol.api.packet

interface NMSPacket {
    fun isCancelled(): Boolean

    fun setCancelled(cancel: Boolean)
}