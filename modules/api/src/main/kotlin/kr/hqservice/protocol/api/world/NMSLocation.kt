package kr.hqservice.protocol.api.world

import kr.hqservice.protocol.api.NMS
import org.bukkit.Location

interface NMSLocation : NMS<Location> {
    fun getWorld(): NMSWorld

    fun setWorld(world: NMSWorld)

    fun getX(): Double

    fun setX(x: Double)

    fun getY(): Double

    fun setY(y: Double)

    fun getZ(): Double

    fun setZ(z: Double)

    fun getYaw(): Float

    fun setYaw(yaw: Float)

    fun getPitch(): Float

    fun setPitch(pitch: Float)

    fun clone(): NMSLocation
}