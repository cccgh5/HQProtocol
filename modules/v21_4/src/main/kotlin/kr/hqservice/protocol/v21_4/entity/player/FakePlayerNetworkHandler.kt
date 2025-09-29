package kr.hqservice.protocol.v21_4.entity.player

import net.minecraft.network.Connection
import net.minecraft.network.protocol.Packet
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ClientInformation
import net.minecraft.server.level.ServerPlayer
import net.minecraft.server.network.CommonListenerCookie
import net.minecraft.server.network.ServerGamePacketListenerImpl

class FakePlayerNetworkHandler(
    server: MinecraftServer,
    connection: Connection,
    entityPlayer: ServerPlayer
) : ServerGamePacketListenerImpl(server, connection, entityPlayer, CommonListenerCookie(entityPlayer.gameProfile, 0, ClientInformation.createDefault(), false)) {
    override fun send(packet: Packet<*>) {}
}