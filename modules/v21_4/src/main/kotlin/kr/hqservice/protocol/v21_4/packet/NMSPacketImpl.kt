package kr.hqservice.protocol.v21_4.packet

import kr.hqservice.protocol.api.packet.NMSPacket
import net.minecraft.network.protocol.Packet

abstract class NMSPacketImpl<T : Packet<*>>(
    private val original: T
) : NMSPacket {
    private var cancelled = false
    private var next = original
    private val extra = mutableListOf<Packet<*>>()

    override fun isCancelled(): Boolean {
        return cancelled
    }

    override fun setCancelled(cancel: Boolean) {
        this.cancelled = cancel
    }

    protected fun updateNext(packet: T) {
        next = packet
    }

    protected fun addExtra(packet: Packet<*>) {
        extra.add(packet)
    }

    fun getOriginal(): T {
        return original
    }

    fun getCurrent(): T {
        return next
    }

    fun getLast(): List<Packet<*>> {
        return listOf(next) + extra
    }
}