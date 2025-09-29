package kr.hqservice.protocol.v21_4.world

import kr.hqservice.protocol.api.world.NMSLocation
import kr.hqservice.protocol.api.world.NMSWorld
import org.bukkit.Location

class NMSLocationImpl(
    private var world: NMSWorldImpl,
    private var x: Double,
    private var y: Double,
    private var z: Double,
    private var yaw: Float,
    private var pitch: Float
) : NMSLocation {
    override fun getWorld(): NMSWorld { return world }
    override fun setWorld(world: NMSWorld) { this.world = world as NMSWorldImpl }

    override fun getX(): Double { return x }
    override fun setX(x: Double) { this.x = x }

    override fun getY(): Double { return y }
    override fun setY(y: Double) { this.y = y }

    override fun getZ(): Double { return z }
    override fun setZ(z: Double) { this.z = z }

    override fun getYaw(): Float { return yaw }
    override fun setYaw(yaw: Float) { this.yaw = yaw }

    override fun getPitch(): Float { return pitch }
    override fun setPitch(pitch: Float) { this.pitch = pitch }

    override fun clone(): NMSLocation { return NMSLocationImpl(world, x, y, z, yaw, pitch) }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is NMSLocationImpl) return false

        if (x != other.x) return false
        if (y != other.y) return false
        if (z != other.z) return false
        if (yaw != other.yaw) return false
        if (pitch != other.pitch) return false
        return true
    }

    override fun hashCode(): Int {
        var result = world.hashCode()
        result = 31 * result + x.hashCode()
        result = 31 * result + y.hashCode()
        result = 31 * result + z.hashCode()
        result = 31 * result + yaw.hashCode()
        result = 31 * result + pitch.hashCode()
        return result
    }

    override fun handle(): Location {
        return Location(world.handle(), x, y, z, yaw, pitch)
    }
}