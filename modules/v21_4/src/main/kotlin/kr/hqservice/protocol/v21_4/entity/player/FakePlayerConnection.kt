package kr.hqservice.protocol.v21_4.entity.player

import net.minecraft.network.Connection
import net.minecraft.network.protocol.PacketFlow

class FakePlayerConnection(
    flow: PacketFlow
) : Connection(flow)