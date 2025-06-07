package dev.znci.rocket.scripting.globals.interfaces.entities

import dev.znci.rocket.scripting.globals.tables.LuaLocation
import dev.znci.rocket.scripting.globals.tables.LuaVector3
import dev.znci.twine.annotations.TwineNativeFunction
import dev.znci.twine.annotations.TwineNativeProperty
import dev.znci.twine.annotations.TwineOverload
import io.papermc.paper.entity.LookAnchor
import org.bukkit.entity.Entity

@Suppress("unused")
interface Entity<T> where T : Entity {
    val entity: T

    @TwineNativeProperty
    val location: LuaLocation
        get() = LuaLocation.fromBukkit(entity.location)

    @TwineNativeFunction
    fun setVelocity(vector: LuaVector3) {
        entity.velocity = vector.toBukkit()
    }

    @TwineNativeProperty
    val velocity: LuaVector3
        get() = LuaVector3.fromBukkit(entity.velocity)

    @TwineNativeProperty
    val height: Double
        get() = entity.height

    @TwineNativeProperty
    val width: Double
        get() = entity.width

    // TODO: Wait for implementation
//    @TwineNativeProperty
//    val boundingBox: LuaBoundingBox

    @TwineNativeProperty
    val onGround: Boolean
        get() = entity.isOnGround

    @TwineNativeProperty
    val inWater: Boolean
        get() = entity.isInWater

    // TODO: Wait for implementation
//    @TwineNativeProperty
//    val world: LuaWorld

    @TwineNativeProperty
    val inWorld: Boolean
        get() = entity.isInWorld

    @TwineNativeFunction
    fun setRotation(yaw: Float, pitch: Float) {
        entity.setRotation(yaw, pitch)
    }

    @TwineNativeFunction
    fun teleport(location: LuaLocation) {
        entity.teleport(location.toBukkit())
    }

    // TODO: Implement LookAnchor!!
    @TwineOverload
    @TwineNativeFunction
    fun lookAt(x: Double, y: Double, z: Double) {
        entity.lookAt(x, y, z, LookAnchor.EYES)
    }

    @TwineOverload
    @TwineNativeFunction
    fun lookAt(vector: LuaVector3) {
        val bukkitVector = vector.toBukkit()
        entity.lookAt(bukkitVector.x, bukkitVector.y, bukkitVector.z, LookAnchor.EYES)
    }

    @TwineNativeProperty
    val entityId: Int
        get() = entity.entityId

    @TwineNativeProperty
    val fireTicks: Int
        get() = entity.fireTicks

    @TwineNativeProperty
    val maxFireTicks: Int
        get() = entity.maxFireTicks

    @TwineNativeFunction
    fun setFireTicks(ticks: Int) {
        entity.fireTicks = ticks
    }

    @TwineNativeFunction
    fun setVisualFire(visible: Boolean) {
        entity.isVisualFire = visible
    }

    @TwineNativeProperty
    val visualFire: Boolean
        get() = entity.isVisualFire

    @TwineNativeProperty
    val freezeTicks: Int
        get() = entity.freezeTicks

    @TwineNativeProperty
    val maxFreezeTicks: Int
        get() = entity.maxFreezeTicks

    @TwineNativeFunction
    fun setFreezeTicks(ticks: Int) {
        entity.freezeTicks = ticks
    }

    @TwineNativeProperty
    val frozen: Boolean
        get() = entity.isFrozen

    @TwineNativeFunction
    fun setInvisible(invisible: Boolean) {
        entity.isInvisible = invisible
    }

    @TwineNativeProperty
    val invisible: Boolean
        get() = entity.isInvisible

    @TwineNativeFunction
    fun setNoPhysics(noPhysics: Boolean) {
        entity.setNoPhysics(noPhysics)
    }

    // noPhysics property is strange, surely it would make more sense to be physics
    // boolean hasNoPhysics();
    @TwineNativeProperty
    val physics: Boolean
        get() = !entity.hasNoPhysics()

    @TwineNativeProperty
    val freezeTickingLocked: Boolean
        get() = entity.isFreezeTickingLocked

    @TwineNativeFunction
    fun setFreezeTickLocked(locked: Boolean) {
        entity.lockFreezeTicks(locked)
    }

    @TwineNativeFunction
    fun remove() {
        entity.remove()
    }

    @TwineNativeProperty
    val dead: Boolean
        get() = entity.isDead

    @TwineNativeProperty
    val valid: Boolean
        get() = entity.isValid

    @TwineNativeProperty
    val persistent: Boolean
        get() = entity.isPersistent

    @TwineNativeFunction
    fun setPersistent(persistent: Boolean) {
        entity.isPersistent = persistent
    }

    // TODO: Implement passengers

    @TwineNativeProperty
    val isEmpty: Boolean
        get() = entity.isEmpty

    @TwineNativeFunction
    fun ejectPassengers() {
        entity.eject()
    }

    @TwineNativeProperty
    val fallDistance: Float
        get() = entity.fallDistance

    @TwineNativeFunction
    fun setFallDistance(distance: Float) {
        entity.fallDistance = distance
    }

    // TODO: Implement EntityDamageEvent

    @TwineNativeProperty
    val uuid: String
        get() = entity.uniqueId.toString()

    @TwineNativeProperty
    val ticksLived: Int
        get() = entity.ticksLived

    @TwineNativeFunction
    fun setTicksLived(ticks: Int) {
        entity.ticksLived = ticks
    }

    // TODO: Implement EntityEffect

    // TODO: Implement EntityType

    // TODO: Implement Sound

    @TwineNativeProperty
    val inVehicle: Boolean
        get() = entity.isInsideVehicle

    @TwineNativeFunction
    fun leaveVehicle() {
        entity.leaveVehicle()
    }

    @TwineNativeProperty
    val vehicle: dev.znci.rocket.scripting.globals.interfaces.entities.Entity<T>?
        get() = entity.vehicle as? dev.znci.rocket.scripting.globals.interfaces.entities.Entity<T>

    @TwineNativeFunction
    fun setNameTagVisible(visible: Boolean) {
        entity.isCustomNameVisible = visible
    }

    @TwineNativeProperty
    val nameTagVisible: Boolean
        get() = entity.isCustomNameVisible

    @TwineNativeFunction
    fun setVisibleByDefault(visible: Boolean) {
        entity.isVisibleByDefault = visible
    }

    @TwineNativeProperty
    val visibleByDefault: Boolean
        get() = entity.isVisibleByDefault

    // TODO: Implement @NotNull Set<Player> getTrackedBy();

    @TwineNativeFunction
    fun setGlowing(glowing: Boolean) {
        entity.isGlowing = glowing
    }

    @TwineNativeProperty
    val glowing: Boolean
        get() = entity.isGlowing

    @TwineNativeFunction
    fun setInvulnerable(invulnerable: Boolean) {
        entity.isInvulnerable = invulnerable
    }

    @TwineNativeProperty
    val invulnerable: Boolean
        get() = entity.isInvulnerable

    @TwineNativeFunction
    fun setSilent(silent: Boolean) {
        entity.isSilent = silent
    }

    @TwineNativeProperty
    val silent: Boolean
        get() = entity.isSilent

    @TwineNativeFunction
    fun setGravity(gravity: Boolean) {
        entity.setGravity(gravity)
    }

    @TwineNativeProperty
    val gravityEnabled: Boolean
        get() = entity.hasGravity()

    @TwineNativeFunction
    fun setPortalCooldown(ticks: Int) {
        entity.portalCooldown = ticks
    }

    @TwineNativeProperty
    val portalCooldown: Int
        get() = entity.portalCooldown

    @TwineNativeProperty
    val scoreboardTags: Set<String>
        get() = entity.scoreboardTags

    @TwineNativeFunction
    fun addScoreboardTag(tag: String) {
        entity.addScoreboardTag(tag)
    }

    @TwineNativeFunction
    fun removeScoreboardTag(tag: String) {
        entity.removeScoreboardTag(tag)
    }

    // TODO: Implement PistonMoveReaction

    // TODO: Implement BlockFace

    // TODO: Implement Pose

    @TwineNativeProperty
    val fixedPose: Boolean
        get() = entity.hasFixedPose()

    @TwineNativeProperty
    val sneaking: Boolean
        get() = entity.isSneaking

    @TwineNativeFunction
    fun setSneaking(sneaking: Boolean) {
        entity.isSneaking = sneaking
    }

    // TODO: Implement SpawnCategory

    @TwineNativeProperty
    val nbtString: String
        get() = entity.asString.toString()

    // TODO: Implement EntitySnapshot

    @TwineNativeFunction
    fun copy(): dev.znci.rocket.scripting.globals.interfaces.entities.Entity<T> {
        return entity.copy() as dev.znci.rocket.scripting.globals.interfaces.entities.Entity<T>
    }

    // TODO: Implement Component

    @TwineNativeProperty
    val origin: LuaLocation
        get() = LuaLocation.fromBukkit(entity.origin!!)

    @TwineNativeProperty
    val fromSpawner: Boolean
        get() = entity.fromMobSpawner()

    // TODO: Implement Chunk

    // TODO: Implement SpawnReason

    @TwineNativeProperty
    val underWater: Boolean
        get() = entity.isUnderWater

    @TwineNativeProperty
    val inRain: Boolean
        get() = entity.isInRain

    // ! Deprecated in 1.21.5
    @TwineNativeProperty
    val inBubbleColumn: Boolean
        get() = entity.isInBubbleColumn

    @TwineNativeProperty
    val inWaterOrRain: Boolean
        get() = entity.isInWaterOrRain

    @TwineNativeProperty
    val inWaterOrBubbleColumn: Boolean
        get() = entity.isInWaterOrBubbleColumn

    @TwineNativeProperty
    val inWaterOrRainOrBubbleColumn: Boolean
        get() = entity.isInWaterOrRainOrBubbleColumn

    @TwineNativeProperty
    val inLava: Boolean
        get() = entity.isInLava

    @TwineNativeProperty
    val ticking: Boolean
        get() = entity.isTicking

    @TwineNativeFunction
    fun spawnAt(location: LuaLocation) {
        entity.spawnAt(location.toBukkit())
    }

    @TwineNativeProperty
    val inPoweredSnow: Boolean
        get() = entity.isInPowderedSnow

    @TwineNativeFunction
    fun doesCollideAt(location: LuaLocation): Boolean {
        return entity.collidesAt(location.toBukkit())
    }
    
    // TODO: Implement BoundingBox

    // TODO: Implement EntityScheduler

    @TwineNativeProperty
    val scoreboardName: String
        get() = entity.scoreboardEntryName
}